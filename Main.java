package main;

import protocol.SipUri;

/**
 *
 * This class is just used to test out the SipUri object by using 8 testing URI
 * strings.
 *
 * @author Logan Stanfield
 * @since 04/12/2018
 * @version 0.2.0
 */
public class Main {

    // Main method.
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        
        String testUri = "sip:alice@atlanta.com";
        String testUri2 = "sip:alice:secretword@atlanta.com;transport=tcp";
        String testUri3 = "sips:alice@atlanta.com?subject=project%20x&priority=urgent";
        /*
         * For this situation, the SIP URL that is being sent is for a phone
         * number with a PIN number. According to the Internet Engineering Task
         * Force (IETF) RFC 3261, the phone number is considered the user and the PIN is
         * considered the password.
         *
         */
        String testUri4 = "sip:+1­212­555­1212:1234@gateway.com;user=phone";
        /*
         * For the following SIP URI, there is either a missing username or a
         * missing password. It would make the most sense for there to be a
         * missing password rather than a username since passwords are generally
         * rely on username and not the other way around. Under this reasoning,
         * the numbers are not treated as a password and treated as a user.
         * Plus, since the RFC 3261 documentation says the password is optional
         * it further makes most sense to consider 1212 as a user rather than
         * a password.
         */
        String testUri5 = "sips:1212@gateway.com";
        /*
         * Dialing by IP address. For this test URI, the host is an IP
         * address. Under SIP URI rules, this is applicable.
         */
        String testUri6 = "sip:alice@192.0.2.4";
        /*
         * This testing URI has no username or password so they remains as the
         * default blank values as indicated in RFC 3261 page 151.
         */
        String testUri7 = "sip:atlanta.com;method=REGISTER?to=alice%40atlanta.com";
        /*
         * This test SIP URI was a bit more tricky to figure out how to handle
         * "day=tuesday", but according to the RFC 3261 page 151, it says semi-
         * colons before the hostname do not be escaped. The recipient shall
         * handle the "day" parameter, but the documentation considers it to
         * be part of the username. Therefore, it will be stored as the user.
         */
        String testUri8 = "sip:alice;day=tuesday@atlanta.com";

        // NON VALID SIP URIs:
        String testUri9 = "sip:user:password:somethingelse@outlook.com";
        String testUri10 = "sipps:user@outlook.com;parameters?headers";
        
        // Declaration and instantiation of the SipUri object.
        SipUri sipUri = new SipUri(testUri3);
        System.out.println(sipUri.toString());
        
        long endTime = System.nanoTime();
        long runtimeDuration = endTime - startTime;
        System.out.println("Duration: " + runtimeDuration);
    }
}
