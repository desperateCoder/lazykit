package controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.tools.shell.Global;

public class LazyPatternInterpreter {
	
	private static final Pattern REGEX_FIELD = Pattern.compile("\\{\\$[ 0-9]*?\\}", Pattern.DOTALL);
	private static final Pattern REGEX_JS_BLOCK = Pattern.compile("\\{JS.*?JS\\}", Pattern.DOTALL);
	

	public LazyPatternInterpreter() {
	}
	
	public String fillPattern(String pattern, String[][] data){
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			//"{$XX}"felder mit den werten ersetzen
			//js interpretieren
			try {
				String tmp = replaceFields(pattern, data[i]);
				ret.append(replaceJS(tmp));				
			} catch (NumberFormatException e){
				JOptionPane.showMessageDialog(null, "Formatfehler:\n\n"+e.getMessage(),
						"Fehler", JOptionPane.ERROR_MESSAGE);
				return "";
			} catch (RuntimeException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "JavaScript-Fehler:\n\n"+e.getMessage(),
											"Fehler", JOptionPane.ERROR_MESSAGE);
				return "";
			}
		}
		
		
		return ret.toString();
	}

	private String interpretJS(String js) throws RuntimeException{
//		System.out.println("JS: "+js);
		Context interpreter = Context.enter();
//		Scriptable scope = interpreter.initStandardObjects();

        Global global = new Global(interpreter);
		try {
			Object result = interpreter.evaluateString(global, js, "<cmd>", 1, null);
			return result.toString();
		} catch (RuntimeException e) {
			throw new RuntimeException(e);
		} finally {
			Context.exit();
		}
	}

	private String replaceFields(String s, String[] data){
		String ret = s;
		Matcher  regexMatcher = REGEX_FIELD.matcher(s);
		while(regexMatcher.find()) {
			String match = regexMatcher.group();
			String content = match.substring(2,match.length()-1).trim();
			int colIndex =  Integer.parseInt(content);
			if (colIndex>=0&&colIndex<data.length) {
				ret = ret.replace(match, data[colIndex]);				
			}
//			System.out.println(content);
		}
		return ret;
	}
	private String replaceJS(String s){
		String ret = s;
		Matcher regexMatcher = REGEX_JS_BLOCK.matcher(s);
		while(regexMatcher.find()) {
			String match = regexMatcher.group();
			String content = match.substring(3,match.length()-3).trim();
			String result = interpretJS(content);
//			System.out.println(Pattern.quote(match));;
			ret = ret.replace(match, result);				
//			System.out.println(content);
		}
		return ret;
	}
	
	public static void main(String[] args) {
//		LazyPatternInterpreter interpreter = new LazyPatternInterpreter();
//		String out = interpreter.interpretJS("str = 'Hello world!';str.substring(1,4); ");
//		System.out.println(out);
//		
//		String s = "das ist ein test{$1}\n und{JS\n" +
//				"\t\r var i = 5;\tvar s='';\n" +
//				"for(;i>=0;i--){\n" +
//				"\tif(i<1){\n" +
//				"\t\ts=s+'ja\\n';\n" +
//				"\t} else {\n" +
//				"\t\ts=s+'{$ 2  }\\n';\n" +
//				"\t}\n" +
//				"JS}" +
//				"und es funzt super" +
//				"{JS\n" +
//				"test\n" +
//				"JS}" +
//				"{$ 3 }" +
//				"{JS\n" +
//				"test2\n" +
//				"JS}";
//		System.out.println(s);
		
		//	({JS {$0}-1 JS}): Hallo {$1}
		
		
//Artur
//Alex
//Domi
//Peter
//Hank	
	}
	
}
