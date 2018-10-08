package protocol;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * This class holds the structure of a SipUri object.
 *
 * General structure as noted by RFC 3261 ->
 * sip:user:password@host:port;uri-parameters?headers
 *
 * SIP URI Connection Types: - Dial by username - Dial by phone number - Dial by
 * IP Address
 *
 * References: https://www.ietf.org/rfc/rfc3261.txt
 * https://msdn.microsoft.com/en-us/library/office/hh347488(v=office.14).aspx
 * https://www.voip-info.org/wiki/view/SIP+URI
 *
 * @author Logan Stanfield
 * @since 04/12/2018
 * @version 0.2.0
 */
public class SipUri {

    private String scheme = "--";
    private String user = "--";
    private String password = "--";
    private String host = "--";
    private String port = "--";
    private String uriParameters = "--";
    private String headers = "--";

    /**
     * Default constructor
     */
    public SipUri() {

    }

    /**
     * This constructor automatically parses the passed in SIP URI sequence.
     *
     * @param sequence The SIP URI sequence
     */
    public SipUri(String sequence) {
        parseSipRegex(sequence);
    }

    /**
     * This method parses the SIP URI sequences that is passed in as a
     * parameter.
     *
     * @param sequence SIP URI string sequence.
     * @since 04/12/18
     * @deprecated use {@link #parseSipRegex(String sequence)} instead.
     */
    @Deprecated
    public void parseSip(String sequence) {

        /*
         * This split separates the sip/sips parameter from the rest of the 
         * sequence. 
         */
        String[] schemeSplit = sequence.split(":", 2);
        scheme = schemeSplit[0];

        // If the scheme is SIP, default port is 5060, 
        if (scheme.equals("sip")) {
            port = "5060";
        } else if (scheme.equals("sips")) {
            port = "5061";
        }

        /*
         * After removing the scheme (sip/sips) remainder of the sequence is
         * is split in half over the @ symbol. The prefix contains the username
         * or password and the suffix contains the host, the port, URI 
         * parameters, and the header.
         */
        String[] prefixSplit = schemeSplit[1].split("@");

        String[] userPasswordSplit = prefixSplit[0].split(":");

        /* If the prefixSplit is equal to the userPasswordSplit there is no
         * no username or password. Therefore, if they are equal
         */
        if (!(Arrays.equals(prefixSplit, userPasswordSplit))) {
            user = userPasswordSplit[0];
        }

        /*
         * If the length of the userPasswordSplit is greater than 1 it means
         * there is a password associated with the username. 
         */
        if (userPasswordSplit.length > 1) {
            password = userPasswordSplit[1];
        }

        /*
         * If the prefixSplit is of length less than or equal to 1 it means
         * the sequence does not contain the user or password prefix.
         */
        String suffix;
        if (prefixSplit.length <= 1) {
            suffix = prefixSplit[0];
        } else {
            /*
             * If there is a user and a password then the suffix is stored in
             * prefixSplit[1] based on the split.
             */
            suffix = prefixSplit[1];
        }
        String[] suffixSplit = suffix.split("\\?");

        /*
         * This is a backwards a approach, but if the suffix splits on a question
         * mark then a header is included. Otherwise the split string will 
         * remian the same.
         */
        if (!suffixSplit[0].equals(suffix)) {
            headers = suffixSplit[1];
            suffix = suffixSplit[0];
        }

        suffixSplit = suffix.split(";");

        /*
         * Continuing the reverse reading of the delimiters, if the string is
         * split on the ; marker it contains URI parameters.
         */
        if (!suffixSplit[0].equals(suffix)) {
            uriParameters = suffixSplit[1];
        }

        // Lastly, add the host.
        host = suffixSplit[0];
    }

    /**
     * This function is a newer adaptation to the parser, to process a SIP URI sequence under regular expression schema.
     * @param sequence SIP URI string sequence.
     * @since 10/08/18
     */
    private void parseSipRegex(String sequence) {
        // Defining the regular expression to define the structure of a SIP URI.
        String regex = "^(sips?)(:)([[\\S]&&[^@:]]*)([;@:])([[\\S]&&[^@\\\\?:]]*)([;@\\\\?]?)([[\\S]&&[^;:]]*)([;\\\\?]?)([[\\S]&&[^\\\\?;:]]*)([\\\\?]?)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher patternMatcher = pattern.matcher(sequence);

        // If there is a matc parse the SIP.
        if (patternMatcher.find()) {
            
            this.scheme = patternMatcher.group(1);

            // Parse SIP according to specification guidline.
            for (int i = 1; i < patternMatcher.groupCount(); i++) {
                
                // Following the "@" is the host.
                if (patternMatcher.group(i).equals("@")) {
                    this.host = patternMatcher.group(i + 1);
                // Following the ";" are the uri-parameters.
                } else if (patternMatcher.group(i).equals(";")) {
                    this.uriParameters = patternMatcher.group(i + 1);
                // Following the "?" are the headers.
                } else if (patternMatcher.group(i).equals("?")) {
                    this.headers = patternMatcher.group(i + 1);
                // If there is a password, it is after the 4th group in the regular expression above.
                } else if (i == 4 && patternMatcher.group(i).equals(":")) {
                    this.password = patternMatcher.group(i + 1);
                } else if (sequence.contains("@") && patternMatcher.group(i).equals(":")) {
                    this.user = patternMatcher.group(i + 1);
                // If the sequence contains no "@", then the host is after the ":".
                } else if (!(sequence.contains("@")) && patternMatcher.group(i).equals(":")) {
                    this.host = patternMatcher.group(i + 1);
                }
            }
            
            // By default, if scheme = "sip" then port == 5060, if scheme = "sips" then port 5061.
            // Unless stated otherwise.
            if (this.scheme.equals("sip")) {
                port = "5060";
            } else if (this.scheme.equals("sips")) {
                port = "5061";
            }
        
        } else {
            System.out.println("Invalid SIP syntax.");
        }
    }

    /**
     *
     * @return SIP URI scheme getter.
     */
    public String getScheme() {
        return scheme;
    }

    /**
     *
     * @return SIP URI user getter.
     */
    public String getUser() {
        return user;
    }

    /**
     *
     * @return SIP URI password getter.
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @return SIP URI host getter.
     */
    public String getHost() {
        return host;
    }

    /**
     *
     * @return SIP URI port getter.
     */
    public String getPort() {
        return port;
    }

    /**
     *
     * @return SIP URI parameter getter.
     */
    public String getUriParameters() {
        return uriParameters;
    }

    /**
     *
     * @return SIP URI header getter.
     */
    public String getHeaders() {
        return headers;
    }

    @Override
    public String toString() {
        String retString = "Scheme: " + scheme + "\n" + "User: " + user + "\n"
                + "Password: " + password + "\n" + "Host: " + host + "\n"
                + "Port: " + port + "\n" + "URI Parameters: " + uriParameters
                + "\n" + "Headers: " + headers + "\n";
        return retString;
    }
}
