package github.tdonuk.notemanager.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {
	private static final TransformerFactory transformerFactory;
	private static final Transformer xmlTransformer;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	static {
		transformerFactory = TransformerFactory.newInstance();
		transformerFactory.setAttribute("indent-number", 4);
		
		try {
			xmlTransformer = transformerFactory.newTransformer(new StreamSource(new File("xml-formatter.xsl")));
			
			xmlTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			xmlTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String formatXml(String raw) throws Exception {
		InputSource src = new InputSource(new StringReader(raw));
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src);
		
		Writer out = new StringWriter();
		
		xmlTransformer.transform(new DOMSource(document), new StreamResult(out));
		
		return out.toString();
	}
	
	public static String formatJson(String raw) throws IOException {
		StringWriter writer = new StringWriter();
		
		objectMapper.writerWithDefaultPrettyPrinter().writeValues(writer).write(objectMapper.readTree(raw));
		
		return writer.toString();
	}
	
	public static String base64Encode(String raw, boolean withPadding){
		if(raw == null || raw.isEmpty()) return raw;
		
		if(withPadding){
			return Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
		}else{
			return Base64.getEncoder().withoutPadding().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
		}
	}
	
	public static String base64Decode(String raw){
		if(raw == null || raw.isEmpty()) return raw;
		
		return new String(Base64.getDecoder().decode(raw));
	}
	
	public static byte[] hashText(String raw) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		return digest.digest(
				raw.getBytes(StandardCharsets.UTF_8));
	}

	public static int length(String text){
		if(text==null) return 0;
		return text.length();
	}

	public static boolean isBlank(String s) {
		return s == null || s.isBlank();
	}

	public static String stringifyToJSON(Object o) throws IOException {
		StringWriter sw = new StringWriter();
		objectMapper.writerWithDefaultPrettyPrinter().writeValue(sw, o);

		return sw.toString();
	}

	public static <T> Object parseJSON(String json, Class<T> clazz) throws JsonProcessingException {
		return objectMapper.readValue(json, clazz);
	}
}
