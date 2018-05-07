# SIP URI Parser #

This is a Java program that I wrote for a job interview that practices parsing
a SIP URI.

* [Getting Started](#gettingstarted)
* [What is it?](#whatisit)
* [The Code](#thecode)

Getting Started <a name="gettingstarted"></a>
===============
This program is written in Java and is available for anybody that needs it. To
test the code simply run the main file and observe the results.

What is it? <a name="whatisit"></a>
===========

The SIP URI scheme is a Uniform Resource Identifier for the Session Initiation
Protocol. SIP is a multimedia protocol used for directing where a communication
takes place.

__The structure is as follows:__ sip:user:password@host:port;uri-parameters?headers

The key is understanding the delimiting characters as shown above.

The Code <a name="thecode"></a>
==============

Below is how you would call the object in the code and read the data.

```Java
String testUri = "sip:alice;day=tuesday@atlanta.com"

// Declaration and instantiation of the SipUri object.
SipUri sipUri = new SipUri(testUri);
System.out.println(sipUri.toString());
```
