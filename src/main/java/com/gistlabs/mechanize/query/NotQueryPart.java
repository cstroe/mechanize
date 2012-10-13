package com.gistlabs.mechanize.query;

/** Query part being a negation. */
class NotQueryPart extends QueryPart {
	private final AbstractQuery<?> query;
	
	public NotQueryPart(boolean isAnd, AbstractQuery<?> query) {
		super(isAnd, (String [])null, null);
		this.query = query;
	}
	
	@Override
	public boolean matches(QueryStrategy queryStrategy, Object object) {
		return !query.matches(queryStrategy, object);
	}
	
	@Override
	public String toString() {
		return "<not" + query.toString() + ">";
	}
}