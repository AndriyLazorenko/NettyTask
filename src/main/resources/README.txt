Comments on methodology of speed calculation.

/**--**/
There were several methodologies concerning speed calculation process for data exchange with server.

The formula used in order to calculate speed of connection was:

            speed = data / timeInterval,

where   data = bytes of data sent via http;
        timeInterval = difference between two <code>System.nanoTime()<code> values, divided by 10^9
        in other words, timeInterval = (endTransfer - startTransfer)/1000000000;

For all the approaches the data value (in bytes) was extracted from Http header in <code>FullHttpResponse</code>
object. The header used for the purpose was (Content-length) header.

Now, the methodology of calculating speed ultimately depends on positions of timers (endTransfer and startTransfer)
Below are descriptions of positions for these timers with some arguments concerning their validity.

1. Between <code>ctx.writeAndFlush(response)</code> block
The idea is: we will get the speed of actual process of sending data.
However, there is an indication, that this method will provide us with shorter time, then needed for a proper data
transfer, as it doesn't include the response formation block of code on server.

2. Starting timer before beginning of response formation, ending timer after flushing the response
This interval will take into account the time it takes for server to form a proper response.
However, the results on speed still outperform ones received via benchmark (5-11 mbps vs 2.5 mbps on benchmark)

3. Another approach to calculating the speed was to set "on" timer earlier on lifecycle of message, and
"off" timer - after flush of response. However, it remains unclear how to get to a point earlier on lifecycle
of message (request in this case)

A textbook approach to measuring Transfer Rate states the following:

<<--Quote begins-->>
TR deals with the velocity of the message transportation
between a client and a server. Two types of requests exist in
SAL: synchronous and asynchronous. TRs for synchronous
requests are measured using two different methods.
Round
Trip Time
 (RTT) measures the average time for sending a
message from the client to the server and back. Two
measuring points are needed on the client side: one before
sending the request and the second directly after receiving the
response.
Throughput
(TP) measures the number of messages
that can be sent from the client to the server and back during a
certain time span. This should be inverse proportional to RTT.
The measuring point for counting the number of received
messages is placed on the client side after receiving the
response from the server.
Testing TR for asynchronous requests require a streaming-
like approach. The
Streaming
 test measures the number of
messages received by a client in a certain time span. After
receiving a request, a server continuously sends messages to a
client until the client requests the server to stop. The
measuring point is implemented on the client side by counting
the number of messages received
<<--Quote ends-->>

All of the above-described approaches use measuring points (in our case - timers) implemented on client side,
however it's quite unclear how to do it with the netty server in our example.

As of now, the 2nd methodology is implemented.

Still, I present you with the full functionality requested by task and hope for the best =)
I believe, that an interesting discussion considering the Transfer Rate measurement will be held if I am
asked to visit main office for final interview.

/**--**/