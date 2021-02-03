# Pulse external module protocol

This document describes the synchronous exchange protocol between external pulse module (slave) and any other client module (master)

## Physical layer

- Connection = UART
- Mode = 8N1
- Baud = 115200
- Maximum interbyte timeout = 200ms

## Message format

    {Code 1b} {ID 1b} {Data length 2b} {Data bytes} {LRC 1b}

```Code``` - command code (should match in request and response)
    
```ID``` - message ID (should match in request and response)
    
```LRC``` - XORed message bytes

## Message acknowledgement

Each message should be responsed by the other side with a valid message or with acknowledgement byte:

- ```ACK (0x00)``` - message successfully received

- ```NAK (0x01)``` - wrong message format, LRC mismatch or command is not supported in current implementation

## PULSE (0x10)

```line (1 byte)``` - line number

```count (2 bytes)``` - сount of pulses (0 for infinite pulses)

```active level (1 byte)``` - each pulse active level: 0 (low) or 1 (high)

```duration (2 bytes)```- pulse duration in milliseconds (see scheme below)

```period (2 bytes)``` - pulse period in milliseconds (see scheme below)

Command is used to put ```count``` pulses on line ```line```. Example for 2 pulse output (count = 2):

```
            <----- period_ms --------->
             ___________________        ___________________
            |                   |      |                   |
    ________|<-- duration_ms -->|______|                   |______  
```

No response message is expected.

## SET (0x11)

```line (1 byte)``` - line number

```level (1 byte)``` - level value: 0 (low) or 1 (high)

Command is used to set ```line``` output to ```level```. No response message is expected.

## GET (0x12)

Command is used to get ```line``` current level

```line (1 byte)``` - line number

Response:

```level (1 byte)``` - level value: 0 (low) or 1 (high)

## LISTEN (0x13)

```line (1 byte)``` - line number

```active level (1 byte)``` - active level value: 0 (low) or 1 (high)

Command is used to subscribe for pulse events on line ```line```. No response message is expected.

## KEYPAD (0x14)

```output line (1 byte)``` - key column line

```input line (1 byte)``` - key row line

```level``` - default level for column line (0 - low, 1 - high)

```key code (1 byte)``` - key code to report in response to POLL.

Command is used to configure GPIO for single matrix keypad key. No response message is expected.

## POLL (0x14)

Get pending events.

Response:

- Keypad event:

```key code (1 byte)```

- Pulse event:

```line (1 byte)``` - line number where pulse event occured

```duration (ms)``` - pulse duration in milliseconds

## LOGS (0x15)

Get system logs file. After sending ACK response transport is switched to file send mode (for UART XModem-1K).
No response message is expected.

### UPDATE (0x16)

Update firmware. After sending ACK response transport is switched to file receive mode (for UART XModem-1K).
No response message is expected.

### VERSION (0x17)

Get firmware version in ```major```.```minor``` format.

Response:

```major (1 byte)``` - major version

```minor (1 byte)``` - minor version
