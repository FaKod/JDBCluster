package org.jdbcluster.filter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.jdbcluster.JDBClusterUtil;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.exception.CCFilterException;
import org.jdbcluster.metapersistence.annotation.DaoLink;
import org.jdbcluster.metapersistence.cluster.Cluster;

/**
 * 
 * @author Christopher Schmidt
 * class CCFilterFactory is responsible for creating
 * CCFilter objects
 * @param <T>
 */
public class CCFilterFactory extends CCFilterBase{
	
	
	/**
	 * creates an Instance of a CCFilter object by passing the Clustertype and the select String
	 * @param ct identifies the ClusterType
	 * @param selId selects the SelectID
	 * @return filter a template which extends CCFilterBase
	 * @throws CCFilterException
	 */
	@SuppressWarnings("unchecked")
	static public <T extends CCFilterBase> T newInstance(ClusterType ct, String selId) throws CCFilterException {
		//get the classname of the object
		String filterClassName = CCFilterBase.getSelect().getClassName(ct, selId);
		if (filterClassName == null) {
			//set the selectstatement, without any bindungs because it's supposed to be 
			//a generic filter
			CCFilterImpl impl = new CCFilterImpl(ct);
			impl.setSelectStatementDAO(ct.getClusterClassName());
			impl.setBinding(new HashMap<String, String>());
			impl.setClassName(CCFilterImpl.class.getName());
			impl.setWhereStatement("");
			return (T) impl; //if no classname was defined, return empty object
		}
		
		String hql = CCFilterBase.getSelect().getWhere(ct, selId);
		HashMap<String, String> binding = CCFilterBase.getSelect().getBinding(ct, selId, filterClassName);

		Class<?> filterClass;
		CCFilterBase filter = null;
		filter = (CCFilterBase) JDBClusterUtil.createClassObject(filterClassName);
		// get select statement
		String selectDAO="";
		String clusterClassName = ct.getClusterClassName();
		try {
			Class<?> clusterClass = Class.forName(clusterClassName, false, Thread.currentThread().getContextClassLoader());
			DaoLink daoLink = clusterClass.getAnnotation(DaoLink.class);
			if(daoLink==null)
				throw new CCFilterException("need " + DaoLink.class.getSimpleName() + "annotation");
			selectDAO = daoLink.dAOClass().getSimpleName();
			
		} catch (ClassNotFoundException e) {
			throw new CCFilterException("no definition for the class [" + clusterClassName + "] with the specified name could be found", e);
		}
		
		//set the filter specific information
		filter.setClusterType(ct);
		filter.setBinding(binding);
		filter.setClassName(filterClassName);
		filter.setWhereStatement(hql);
		filter.setSelectStatementDAO(selectDAO);
		return (T) filter;
	}
		
}