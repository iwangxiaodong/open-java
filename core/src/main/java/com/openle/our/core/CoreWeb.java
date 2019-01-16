package com.openle.our.core;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 *
 * @author 168
 */
public class CoreWeb {

    public static String getHtmlByUrl(String url) throws MalformedURLException, IOException {
        StringBuilder sb = new StringBuilder();
        for (Scanner sc = new Scanner(new URL(url).openStream()); sc.hasNext();) {
            sb.append(sc.nextLine()).append('\n');
        }
        return sb.toString();
    }

    // Or org.jsoup.parser.Parser.unescapeEntities(String string, boolean inAttribute)
    // StringEscapeUtils.unescapeHtml(...) is deprecated
    public static String unescapeHtml3(String str) {
        try {
            Class<?> clazz = Class.forName("javax.swing.text.html.HTMLDocument");
            Constructor c = clazz.getConstructor();
            Object doc = c.newInstance();

            Class<?> editorClass = Class.forName("javax.swing.text.html.HTMLEditorKit");
            Constructor c1 = editorClass.getConstructor();
            Object editor = c1.newInstance();

            Method m = editorClass.getDeclaredMethod("read", StringReader.class.getSuperclass(),
                    clazz.getSuperclass().getSuperclass().getInterfaces()[0], int.class);
            m.invoke(editor, new StringReader(str), doc, 0);

            Method getLength = clazz.getSuperclass().getSuperclass()
                    .getDeclaredMethod("getLength");
            int length = (int) getLength.invoke(doc);

            Method getText = clazz.getSuperclass().getSuperclass()
                    .getDeclaredMethod("getText", int.class, int.class);
            String r = (String) getText.invoke(doc, 0, length);
            return r;
        } catch (ReflectiveOperationException e) {
            System.err.println(e);
        }

//        HTMLDocument doc = new HTMLDocument();
//        try {
//            new HTMLEditorKit().read(new StringReader("<html><body>" + str), doc, 0);
//            return doc.getText(1, doc.getLength());
//        } catch (IOException | BadLocationException ex) {
//            System.out.println(ex);
//        }
        return null;
    }
}
