package org.jdbcluster.metapersistence.aspects;

import org.aspectj.lang.reflect.FieldSignature;
import org.jdbcluster.metapersistence.annotation.NoDAO;
import org.jdbcluster.metapersistence.cluster.Cluster;
import org.jdbcluster.metapersistence.cluster.ClusterBase;
import org.jdbcluster.template.ConfigurationFactory;
import org.jdbcluster.template.ConfigurationTemplate;
import org.aspectj.lang.annotation.*;

/**
 * Aspect to automatically cut the length of a attribute to the max length
 */

public aspect ClusterAttributeStringLength {

	/**
	 * first aspect
	 */
	declare precedence: ClusterAttribute;

	/**
	 * pointcut for all sets on Dao String attribute
	 */
	pointcut checkRange(ClusterBase c, String s):
			set(String Cluster+.*) && !@annotation(NoDAO) && target(c) && args(s);

	/**
	 * advice uses currenty HibernateConfiguration to get max String length
	 */
	@SuppressAjWarnings("adviceDidNotMatch")
	Object around(ClusterBase c, String s):checkRange(c, s) {
		if (ConfigurationFactory.isConfigured() && s != null) {
			ConfigurationTemplate cf = ConfigurationFactory.getInstance();
			FieldSignature sig = (FieldSignature) thisJoinPointStaticPart.getSignature();
			int maxLength = cf.getLenthOfStringAttribute((Cluster) c, sig.getField().getName());
			if (maxLength < s.length()) {
				return proceed(c, s.substring(0, maxLength));
			}
		}
		return proceed(c, s);
	}

}
