package org.usayi.preta;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AbysterTest
{

	@Test
	@SuppressWarnings( "unused")
	public void test()
	{
		final String abysterPaiementUrl = "http://sandbox-abyster.appspot.com/rest/api/v2/transaction/bill";
		final String abysterPaiementUrlProd = "http://www.abyster.com/rest/api/v2/transaction/bill";
		
		/* Abyster Sandbox COnfig */
		final String consumerId = "5727217287954432";
		final String consumerIdProd = "5727217287954432";
		
		final String consumerSecret = "OBU65E4SQIQLFKMFOD3NLTL5R926F-95730917-b3ae-43a5-82ec-aa2192bb66c7";
		final String consumerSecretProd = "OBU65E4SQIQLFKMFOD3NLTL5R926F-95730917-b3ae-43a5-82ec-aa2192bb66c7";
		
		final String consumerName = "Preta";
		final String consumerEmail = "usayi.technology@gmail.com";
		
		final String consumerTmstmp =  new SimpleDateFormat( "yyyyMMddhhmmss").format( new Date()) + "000";
		
		/* Compute Token */
		String consumerToken = "";
		MessageDigest md;
		try
		{
			md = MessageDigest.getInstance( "MD5");
			
			byte byteData[] = md.digest( (consumerId + consumerTmstmp + consumerSecret).getBytes());
			
			StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < byteData.length; i++) {
	         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
	        }

	        consumerToken = sb.toString();
	        
		} catch (NoSuchAlgorithmException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println( "Consumer Token => " + consumerToken);
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost postRequest = new HttpPost( abysterPaiementUrl);
		
		/* Header Config */
		postRequest.addHeader( "Accept", "application/json");
		postRequest.addHeader( "Accept-Encoding", "gzip,deflate");
		postRequest.addHeader( "Content-Type", "application/json");
		postRequest.addHeader( "Authorization", "AUTH consumerEmail=\"" +
								consumerEmail + "\",consumerTimestamp=\"" +
								consumerTmstmp + "\",consumerToken=\"" + 
								consumerToken + "\",consumerName=\"" +
								consumerName + "\"");
		
		/* Paiement Request */
		Map<String, String> order = new HashMap< String, String>();
		order.put( "reference", "TEST000");
		order.put( "redirectUrl", "http://localhost:8080/preta");
		
		Map<String, String> amount = new HashMap<String, String>();
		amount.put( "amountTTC", "500");
		amount.put( "currency", "XAF");
		amount.put( "paymentMethod", "TRANSFERT_MOBILE_MONEY");
		
		/* Client */
		Map<String, String> buyer = new HashMap<String, String>();
		buyer.put( "msisdn", "0022961242984");
		buyer.put( "email", "dosssman@hotmail.fr");
		buyer.put( "name", "Julien");
		buyer.put( "operateur", "OrangeMoney");
		
		/* Admin */
		Map<String, String> admin = new HashMap<String, String>();
		admin.put( "msisdn", "0022962168107");
		admin.put( "email", "usayi.technology@gmail.com");
		buyer.put( "name", "Preta Admin");
		buyer.put( "operateur", "OrangeMoney");
		
		/* Receiver */
		Map<String, Object> receiver = new HashMap<String, Object>();
		receiver.put( "buyer", admin);
		receiver.put( "amount", amount);
		receiver.put( "description", "Tests Abyster API");
		
		/* Receivers */
		List<Map<String, Object>> receivers = new ArrayList<Map<String,Object>>();
		receivers.add( receiver);
		
		/* Root Config */
		Map<String, Object> root = new HashMap<String, Object>();
		root.put( "order", order);
		root.put( "amount", amount);
		root.put( "buyer", buyer);
		root.put( "receiver", receivers);
		
		/* JSON Conversion */
		ObjectMapper mapper = new ObjectMapper();
		try
		{
			StringEntity dataString = new StringEntity( mapper.writeValueAsString( root));
			System.out.println( dataString);
			
			/* Paiement Request */
			
			postRequest.setEntity( dataString);
			
			org.apache.http.HttpResponse httpResponse = httpClient.execute( postRequest);
			
			int responseCode = httpResponse.getStatusLine().getStatusCode();

			System.out.println("Response Code : " + responseCode);

			BufferedReader rd = new BufferedReader(
		                new InputStreamReader(httpResponse.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			System.out.println(result.toString());
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
