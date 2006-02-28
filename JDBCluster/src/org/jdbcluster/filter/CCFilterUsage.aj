package org.jdbcluster.filter;

public aspect CCFilterUsage {
	pointcut newCCFilter(): 
		call(CCFilter+.new(..)) && 
		!withincode(* CCFilterFactory.*(..));

	declare error: newCCFilter(): 
		"please use static factory method CCFilterFactory.newInstance()";
}
