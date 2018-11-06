package push.tester.utils;

public class ArgsDrive {
	public static void main(String[] args) {
		Args arg;
		try {
			arg = new Args("s,l,m*,p#,i*", args);
			boolean logging = arg.getBoolean('l');
			boolean useTls = arg.getBoolean('s');
			int port = arg.getInt('p');
			String ip = arg.getString('i');
			System.out.printf("connect %s:%d, logging : %b, TLS : %b\n", ip, port, logging, useTls);
		} catch (ArgsException e) {
			System.out.printf("Argument error : %s\n", e.errorMessage());
		}
	}
}
