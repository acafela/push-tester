package push.tester.utils;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.Test;

import push.tester.utils.old.Args_old;

public class ArgsTests {
	
	@Test
	public void test() throws ParseException {
		String[] args = new String[] {"ls", "-la", "-m", "ok"};
		Args_old arg = new Args_old("l,a,s,b,m*", args);

		System.out.println(arg.getString('m'));
		
		assertTrue(arg.getBoolean('l'));
		assertTrue(arg.getBoolean('a'));
		assertFalse(arg.getBoolean('s'));
		assertFalse(arg.getBoolean('b'));
		assertEquals("ok", arg.getString('m'));
	}

}
