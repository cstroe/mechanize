package com.gistlabs.mechanize.json.query;

import static org.junit.Assert.*;

import java.util.List;

import org.json.JSONObject;
import org.junit.Test;

import com.gistlabs.mechanize.json.Node;
import com.gistlabs.mechanize.json.nodeImpl.ObjectNodeImpl;

public class NodeSelectorTest {
	
	@Test
	public void testStar() throws Exception {
		ObjectNodeImpl node = new ObjectNodeImpl(new JSONObject("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }"));
		NodeSelector selector = new NodeSelector(node);
		
		List<Node> result = selector.findAll("*");
		assertEquals(4, result.size());
	}
	
	@Test
	public void testName() throws Exception {
		ObjectNodeImpl node = new ObjectNodeImpl(new JSONObject("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }"));
		NodeSelector selector = new NodeSelector(node);
		
		List<Node> result = selector.findAll("b");
		assertEquals(1, result.size());
	}

	@Test
	public void testAttributePresent() throws Exception {
		ObjectNodeImpl node = new ObjectNodeImpl(new JSONObject("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }"));
		NodeSelector selector = new NodeSelector(node);
		
		List<Node> result = selector.findAll("b[x]");
		assertEquals(1, result.size());
		assertEquals("y", result.get(0).getAttribute("x"));
		
		assertTrue(selector.findAll("b[z]").isEmpty());
	}

	@Test
	public void testAttributeEquals() throws Exception {
		ObjectNodeImpl node = new ObjectNodeImpl(new JSONObject("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }"));
		NodeSelector selector = new NodeSelector(node);
		
		List<Node> result = selector.findAll("b[x=\"y\"]");
		assertEquals(1, result.size());
		assertEquals("y", result.get(0).getAttribute("x"));
		
		assertTrue(selector.findAll("b[x=\"z\"]").isEmpty());
	}

	@Test
	public void testAttributeTilda() throws Exception {
		ObjectNodeImpl node = new ObjectNodeImpl(new JSONObject("{ \"a\" : 2, \"b\" : { \"x\" : \"y foo bar\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }"));
		NodeSelector selector = new NodeSelector(node);
		
		assertEquals(1, selector.findAll("b[x~=\"y\"]").size());
		assertEquals(1, selector.findAll("b[x~=\"foo\"]").size());
		assertEquals(1, selector.findAll("b[x~=\"bar\"]").size());

		assertEquals(0, selector.findAll("b[x~=\"baz\"]").size());
	}

	@Test
	public void testWildcardWithAttribute() throws Exception {
		ObjectNodeImpl node = new ObjectNodeImpl(new JSONObject("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }"));
		NodeSelector selector = new NodeSelector(node);
		
		List<Node> result = selector.findAll("*[x]");
		assertEquals(1, result.size());
		assertEquals("y", result.get(0).getAttribute("x"));
		
		assertTrue(selector.findAll("b[z]").isEmpty());
	}
	@Test
	public void testNot() throws Exception {
		ObjectNodeImpl node = new ObjectNodeImpl(new JSONObject("{ \"a\" : 2, \"b\" : { \"x\" : \"y\" }, \"results\" : [ { \"a\" : 1 }, { \"b\" : 2 } ] }"));
		NodeSelector selector = new NodeSelector(node);
		
		List<Node> result = selector.findAll("*:not([x])");
		assertEquals(2, result.size());
		assertEquals("1", result.get(0).getAttribute("a"));
		
		List<Node> result2 = selector.findAll("*:not([x])");
		assertEquals(2, result2.size());
		assertEquals("1", result2.get(0).getAttribute("a"));
	}	
}

