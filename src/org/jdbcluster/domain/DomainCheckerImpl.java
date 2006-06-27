/*
 * Copyright 2002-2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbcluster.domain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.jdbcluster.JDBClusterUtil;
import org.jdbcluster.exception.ConfigurationException;
import org.jdbcluster.exception.DomainException;
import org.jdbcluster.metapersistence.annotation.AddDomainDependancy;
import org.jdbcluster.metapersistence.annotation.Domain;
import org.jdbcluster.metapersistence.annotation.DomainDependancy;
import org.jdbcluster.metapersistence.annotation.PrivilegesDomain;
import org.jdbcluster.metapersistence.cluster.ClusterBase;
import org.jdbcluster.privilege.PrivilegeChecker;
import org.jdbcluster.privilege.PrivilegeCheckerImpl;
import org.springframework.util.Assert;

/**
 * checks slave domains for valid String values (I am really unlucky with this
 * code) 1. valid or invalid tags with value attribute 2. valid tags with all
 * attribute 3. invalid tags with all attribute
 * 
 * @author Christopher Schmidt
 * @author FaKod
 */
public class DomainCheckerImpl extends DomainBase implements DomainChecker {

	/**
	 * Logger instance
	 */
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * private ctor
	 */
	private DomainCheckerImpl() {
	}

	/**
	 * public instance getter
	 * 
	 * @return DomainChecker
	 */
	public static DomainChecker getInstance() {
		return getImplInstance();
	}

	/**
	 * package instance getter
	 * 
	 * @return DomainCheckerImpl
	 */
	static DomainCheckerImpl getImplInstance() {
		return new DomainCheckerImpl();
	}

	/**
	 * returns the corresponding domain id
	 * 
	 * @param cluster Cluster object to use
	 * @param propPath path to the master or slave property
	 * @return String domain id
	 */
	public String getDomainId(ClusterBase cluster, String propPath) {
		
		Assert.notNull(cluster, "ClusterBase may not be null");
		Assert.hasLength(propPath, "propPath may not be null");
		
		if (logger.isDebugEnabled())
			logger.debug("getDomainId for cluster [" + cluster.getClass().getName() + "] and Property Path [" + propPath + "]");

		String domId;
		Field fSlave = JDBClusterUtil.getField(propPath, cluster);

		DomainDependancy dd = fSlave.getAnnotation(DomainDependancy.class);
		if (dd == null) {
			Domain d = fSlave.getAnnotation(Domain.class);
			if (d == null)
				throw new ConfigurationException("no annotation @DomainDependancy or @Domain found on: " + propPath);
			domId = d.domainId();
		} else
			domId = dd.domainId();

		if (logger.isDebugEnabled())
			logger.debug("returning domain id [" + domId + "]");

		return domId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jdbcluster.domain.DomainChecker#check(org.jdbcluster.metapersistence.cluster.ClusterBase,
	 *      java.lang.String)
	 */
	public boolean check(ClusterBase cluster, String propSlavePath) {

		Assert.notNull(cluster, "ClusterBase may not be null");
		Assert.hasLength(propSlavePath, "propSlavePath may not be null");

		Field fSlave = JDBClusterUtil.getField(propSlavePath, cluster);
		String slaveValue = (String) JDBClusterUtil.invokeGetPropertyMethod(propSlavePath, cluster);
		DomainDependancy ddSlave = fSlave.getAnnotation(DomainDependancy.class);

		if (logger.isDebugEnabled())
			logger.debug("check for cluster [" + cluster.getClass().getName() + "] and Property Slave Path [" + propSlavePath + "] and Slave Value [" + slaveValue + "] and Domain Dependancy Domain Id ["
					+ ddSlave.domainId() + "]");

		return check(cluster, fSlave, slaveValue, ddSlave);
	}

	/**
	 * for package wide use
	 * 
	 * @param cluster cluster instance
	 * @param fSlave field of the slave domain
	 * @param slaveValue the value to check
	 * @param ddSlave the annotation
	 * @return true if value is allowed
	 */
	boolean check(ClusterBase cluster, Field fSlave, String slaveValue, DomainDependancy ddSlave) {

		String slaveDomainId = getDomainIdFromField(fSlave, true);
		String propMasterPath = ddSlave.dependsFromProperty();

		Field fMaster = JDBClusterUtil.getField(propMasterPath, cluster);
		String masterDomainId = getDomainIdFromField(fMaster, false);

		String masterValue = (String) JDBClusterUtil.invokeGetPropertyMethod(ddSlave.dependsFromProperty(), cluster);

		// check if there are additional depenancies
		if (!fSlave.isAnnotationPresent(AddDomainDependancy.class))
			return check(masterDomainId, masterValue, slaveDomainId, slaveValue);

		AddDomainDependancy aDD = fSlave.getAnnotation(AddDomainDependancy.class);
		String[] addDomIdArr = new String[aDD.addDepFromProp().length];
		String[] addDomValArr = new String[aDD.addDepFromProp().length];
		for (int i = 0; i < aDD.addDepFromProp().length; i++) {

			Field faddMaster = JDBClusterUtil.getField(aDD.addDepFromProp()[i], cluster);
			String addMasterDomainId = getDomainIdFromField(faddMaster, false);

			addDomIdArr[i] = addMasterDomainId;
			addDomValArr[i] = (String) JDBClusterUtil.invokeGetPropertyMethod(aDD.addDepFromProp()[i], cluster);
		}
		return check(masterDomainId, masterValue, slaveDomainId, slaveValue, addDomIdArr, addDomValArr);
	}

	/**
	 * Main method for domain checking
	 * 
	 * @param masterDomainId configured master domain id (fe. "ColorTypeDomain")
	 * @param masterValue the master entry (fe. "Color", "BlackWhite")
	 * @param slaveDomainId configured slave domain id (fe. "ColorDomain")
	 * @param slaveValue the slave entry (fe. "RED", "BLACK")
	 * @return true if the slave value is allowed. Otherwise false (quel surprise)
	 */
	boolean check(String masterDomainId, String masterValue, String slaveDomainId, String slaveValue) {
		if (!checkAgainstPossibleDomainEntries(slaveDomainId, slaveValue))
			return false;

		EntrySet es = getDomainConfig().getEntrySet(masterDomainId);
		if (es == null)
			throw new ConfigurationException("while checking domainId [" + slaveDomainId + "] and value [" + slaveValue + "]" + " master domain [" + masterDomainId
					+ "] annotated [@Domain] but not configured");

		HashMap<String, ValidEntryList> slaveEntries = es.get(masterValue == null ? "" : masterValue);
		if (slaveEntries == null) {
			if (es.get("*") == null)
				throw new DomainException("while checking domainId [" + slaveDomainId + "] and value [" + slaveValue + "]" + " master domain [" + masterDomainId + "]: value [" + masterValue
						+ "] is not configured");
			else
				return true; // wildcard match
		}
		return validate(slaveEntries.get(slaveDomainId), slaveValue);
	}

	/**
	 * Main method for domain checking with additional master
	 * 
	 * @param masterDomainId configured master domain id (fe. "ColorTypeDomain")
	 * @param masterValue the master entry (fe. "Color", "BlackWhite")
	 * @param slaveDomainId configured slave domain id (fe. "ColorDomain")
	 * @param slaveValue the slave entry (fe. "RED", "BLACK")
	 * @param addMasterDomainId array of additional master ids
	 * @param addMasterValue array of additional master values
	 * @return true if the slave value is allowed. Otherwise false (quel surprise)
	 */
	boolean check(String masterDomainId, String masterValue, String slaveDomainId, String slaveValue, String[] addMasterDomainId, String[] addMasterValue) {
		if (!checkAgainstPossibleDomainEntries(slaveDomainId, slaveValue))
			return false;

		EntrySet es = getDomainConfig().getEntrySet(masterDomainId);
		if (es == null)
			throw new ConfigurationException("while checking domainId [" + slaveDomainId + "] and value [" + slaveValue + "]" + "master domain [" + masterDomainId
					+ "] annotated [@Domain] but not configured");

		if (masterValue == null)
			masterValue = "";

		HashMap<String, ValidEntryList> slaveEntries = es.get(masterValue);
		if (slaveEntries == null) {
			if (es.get("*") == null)
				throw new DomainException("while checking domainId [" + slaveDomainId + "] and value [" + slaveValue + "]" + "master domain [" + masterDomainId + "]: value [" + masterValue
						+ "] is not configured");
			else
				return true; // wildcard match
		}
		boolean hadInvalidEntries = false;
		boolean hadValidEntries = false;
		ValidEntryList vel = slaveEntries.get(slaveDomainId);
		ArrayList<ValidEntryList> v = vel.getValidFromDomainEntry(addMasterDomainId, addMasterValue);
		for (int i = v.size(); i > 0;) {
			ValidEntryList ved = v.get(--i);
			Valid validDomEntry = ved.getValidFromDomainEntry(slaveValue);
			if (validDomEntry != null) {
				return validDomEntry.valid;
			}
			if (!ved.isEmpty())
				return validate(ved, slaveValue);

			if (ved.isContainsInValidElements())
				hadInvalidEntries = true;
			if (ved.isContainsValidElements())
				hadValidEntries = true;
		}
		if (hadInvalidEntries && !hadValidEntries)
			return true;
		if (!hadInvalidEntries && !hadValidEntries)
			return true;
		if (!hadInvalidEntries && hadValidEntries)
			return false;
		if (hadInvalidEntries && hadValidEntries)
			return false;
		return false;
	}

	/**
	 * IMPLEMENTATION calculates set of valid domain values (master or slave)
	 * 
	 * @param cluster Cluster object to use
	 * @param propPath path to the master or slave property
	 * @return ValidDomainEntries<String> with the valid domain values
	 */
	public ValidDomainEntries<String> getValidDomainEntries(ClusterBase cluster, String propPath) {

		Assert.notNull(cluster, "ClusterBase may not be null");
		Assert.hasLength(propPath, "propPath may not be null");

		if (logger.isDebugEnabled())
			logger.debug("getValidDomainEntries for Cluster [" + cluster.getClass().getName() + "]" + " and property path [" + propPath + "]");

		String masterDomainId;
		String masterValue;
		String slaveDomainId;

		Field fMaster;
		Field fSlave;

		fSlave = JDBClusterUtil.getField(propPath, cluster);

		DomainDependancy dd = fSlave.getAnnotation(DomainDependancy.class);
		if (dd == null) {
			Domain d = fSlave.getAnnotation(Domain.class);
			if (d == null)
				throw new ConfigurationException("no annotation @DomainDependancy or @Domain found on: " + propPath);

			if (logger.isDebugEnabled())
				logger.debug("getValidDomainEntries doing rightsIntersection for domain id[" + d.domainId() + "]" + " and Field [" + fSlave.getName() + "]");

			return rightsIntersection(getPossibleDomainEntries(d.domainId()), d.domainId(), fSlave);
		}
		slaveDomainId = dd.domainId();

		String propMasterPath = dd.dependsFromProperty();
		fMaster = JDBClusterUtil.getField(propMasterPath, cluster);
		masterDomainId = getDomainIdFromField(fMaster, false);

		masterValue = (String) JDBClusterUtil.invokeGetPropertyMethod(dd.dependsFromProperty(), cluster);
		if (!fSlave.isAnnotationPresent(AddDomainDependancy.class)) {

			if (logger.isDebugEnabled())
				logger.debug("getValidDomainEntries doing rightsIntersection for master domain id[" + masterDomainId + "]" + " and masterValue [" + masterValue + "]" + " and slaveDomainId [" + slaveDomainId
						+ "] and Field [" + fSlave.getName() + "]");

			return rightsIntersection(getValidDomainEntries(masterDomainId, masterValue, slaveDomainId), slaveDomainId, fSlave);
		}

		AddDomainDependancy aDD = fSlave.getAnnotation(AddDomainDependancy.class);
		String[] addDomIdArr = new String[aDD.addDepFromProp().length];
		String[] addDomValArr = new String[aDD.addDepFromProp().length];
		for (int i = 0; i < aDD.addDepFromProp().length; i++) {

			Field faddMaster = JDBClusterUtil.getField(aDD.addDepFromProp()[i], cluster);
			String addMasterDomainId = getDomainIdFromField(faddMaster, false);

			addDomIdArr[i] = addMasterDomainId;
			addDomValArr[i] = (String) JDBClusterUtil.invokeGetPropertyMethod(aDD.addDepFromProp()[i], cluster);
		}

		if (logger.isDebugEnabled())
			logger.debug("getValidDomainEntries (additional master) doing rightsIntersection for master domain id[" + masterDomainId + "]" + " and masterValue [" + masterValue + "]"
					+ " and slaveDomainId [" + slaveDomainId + "] and Field [" + fSlave.getName() + "]");

		return rightsIntersection(getValidDomainEntries(masterDomainId, masterValue, slaveDomainId, addDomIdArr, addDomValArr), slaveDomainId, fSlave);
	}

	/**
	 * calculates set of valid domain values (master or slave)
	 * 
	 * @param masterDomainId master domain id
	 * @param masterValue master value
	 * @param slaveDomainId slave domain id
	 * @param addDomIdArr array of additional master domain ids
	 * @param addDomValArr array of additional master domain values
	 * @return ValidDomainEntries<String> of all valid domain entries
	 */
	ValidDomainEntries<String> getValidDomainEntries(String masterDomainId, String masterValue, String slaveDomainId, String[] addDomIdArr, String[] addDomValArr) {
		ValidDomainEntries<String> wholeList = getValidDomainEntries(masterDomainId, masterValue, slaveDomainId);
		ValidDomainEntries<String> possibleValueList = getPossibleDomainEntries(slaveDomainId);

		EntrySet es = getDomainConfig().getEntrySet(masterDomainId);
		if (es == null)
			throw new ConfigurationException("slave domain [" + slaveDomainId + "].\n master domain [" + masterDomainId + "] annotated [@Domain] but not configured");

		if (masterValue == null)
			masterValue = "";

		HashMap<String, ValidEntryList> slaveEntries = es.get(masterValue);
		if (slaveEntries == null) {
			EntrySet slaveEs = getDomainConfig().getEntrySet(slaveDomainId);
			if (slaveEs.get("*") == null)
				throw new DomainException("slave domain [" + slaveDomainId + "].\n master domain [" + masterDomainId + "]: value [" + masterValue + "] is not configured");
			else
				return wholeList; // wildcard match
		}

		ValidDomainEntries<String> resultList = new ValidDomainEntries<String>();
		ValidEntryList valid = slaveEntries.get(slaveDomainId);

		for (String domEntry : wholeList) {
			if (validate(valid, domEntry)) {
				resultList.add(domEntry);
			}
		}
		if (validate(valid, null)) {
			resultList.setNullAllowed(true);
		}

		ArrayList<ValidEntryList> al = valid.getValidFromDomainEntry(addDomIdArr, addDomValArr);
		for (int i = 0; i < al.size(); i++) {
			ValidEntryList ved = al.get(i);

			HashMap<String, Valid> mapToValids = ved.getMapValuesToValid();
			for (String s : mapToValids.keySet()) {
				Valid v = mapToValids.get(s);
				if (v.valid) {
					if (!resultList.contains(s))
						if (possibleValueList.contains(s))
							resultList.add(s);
				} else {
					resultList.remove(s);
				}
			}

			for (Valid v : ved) {
				if (v.valid) {
					if (v.value == null)
						resultList.setNullAllowed(v.nullValue);
					else if (!resultList.contains(v.value))
						resultList.add(v.value);
				} else {
					if (v.value == null)
						resultList.setNullAllowed(!v.nullValue);
					else
						resultList.remove(v.value);
				}
			}
		}
		return resultList;
	}

	/**
	 * returns all valid values for domain slaveDomainId
	 * 
	 * @param masterDomainId the master entry (fe. "Color", "BlackWhite")
	 * @param masterValue the master entry (fe. "Color", "BlackWhite")
	 * @param slaveDomainId configured slave domain id (fe. "ColorDomain")
	 * @return ordered list of valid domain entries
	 */
	ValidDomainEntries<String> getValidDomainEntries(String masterDomainId, String masterValue, String slaveDomainId) {
		ValidDomainEntries<String> wholeList = getPossibleDomainEntries(slaveDomainId);

		EntrySet es = getDomainConfig().getEntrySet(masterDomainId);
		if (es == null)
			throw new ConfigurationException("slave domain [" + slaveDomainId + "].\n master domain [" + masterDomainId + "] annotated [@Domain] but not configured");

		if (masterValue == null)
			masterValue = "";

		HashMap<String, ValidEntryList> slaveEntries = es.get(masterValue);
		if (slaveEntries == null) {
			EntrySet slaveEs = getDomainConfig().getEntrySet(slaveDomainId);
			if (slaveEs.get("*") == null)
				throw new DomainException("slave domain [" + slaveDomainId + "].\n master domain [" + masterDomainId + "]: value [" + masterValue + "] is not configured");
			else
				return wholeList; // wildcard match
		}

		ValidDomainEntries<String> resultList = new ValidDomainEntries<String>();
		ValidEntryList valid = slaveEntries.get(slaveDomainId);

		for (String domEntry : wholeList) {
			if (validate(valid, domEntry)) {
				resultList.add(domEntry);
			}
		}
		if (validate(valid, null)) {
			resultList.setNullAllowed(true);
		}
		return resultList;
	}

	/**
	 * returns whole list for the specified domain
	 * 
	 * @param domainId domain id
	 */
	public ValidDomainEntries<String> getPossibleDomainEntries(String domainId) {
		return new ValidDomainEntries<String>(getDomainListInstance(domainId).getDomainEntryList(domainId));
	}

	/**
	 * checks the given slave value against all possible values
	 * 
	 * @param slaveDomainId slave domain id
	 * @param slaveValue slave domain value
	 * @return true if value is allowed
	 */
	boolean checkAgainstPossibleDomainEntries(String slaveDomainId, String slaveValue) {
		ValidDomainEntries<String> vde = getPossibleDomainEntries(slaveDomainId);
		if (slaveValue == null)
			return vde.isNullAllowed();
		if (vde.contains(slaveValue))
			return true;
		return false;
	}

	/**
	 * inersect all domain values with user rights if the richts are not
	 * sufficient, the values will be removed
	 * 
	 * @param vde the list of domain values
	 * @param domainId the domain id
	 * @return
	 */
	ValidDomainEntries<String> rightsIntersection(ValidDomainEntries<String> vde, String domainId, Field f) {
		if (f.isAnnotationPresent(PrivilegesDomain.class)) {
			PrivilegeChecker pc = PrivilegeCheckerImpl.getInstance();
			ArrayList<String> al = new ArrayList<String>();
			for (String value : vde) {
				if (!pc.userPrivilegeIntersectDomain(domainId, value))
					al.add(value);
			}
			vde.removeAll(al);
		}
		return vde;
	}

}
