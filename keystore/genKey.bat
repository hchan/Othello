set KEYSTORE=signing-jar.keystore
keytool -genkey -alias applet -keystore %KEYSTORE% -storepass applet -keypass applet -dname "CN=developer, OU=group 3, O=org.apache.hchan, L=Somewhere, ST=Germany, C=DE"
keytool -selfcert -alias applet -keystore %KEYSTORE% -storepass applet -keypass applet