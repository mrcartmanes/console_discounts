# Pulse external module protocol

This document describes the synchronous exchange protocol between external pulse module (slave) and any other client module (master)

## Physical layer

- Connection = UART
- Mode = 8N1
- Baud = 115200
- Maximum interbyte timeout = 200ms

## Message format

    {Code 1b} {ID 1b} {Data length 2b} {Data bytes} {LRC 1b}

```Code``` - command code
    
```ID``` - message ID should match in request and response
    
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
 
Example for 2 pulse output (count = 2):
```
            <----- period_ms --------->
             ___________________        ___________________
            |                   |      |                   |
    ________|<-- duration_ms -->|______|                   |______  
```

No response message is expected.

### SET (0x11)

Command is used to set ```line``` output to ```level```

```OUT (0x11)``` - command code
```line (1 byte)``` - line number
```level (1 byte)``` - level value: 0 (low) or 1 (high)

##### Response:
```ACK```

### GET (0x12)

Command is used to get ```line``` current level

```GET (0x12)``` - command code
```line (1 byte)``` - line number

##### Response:

```GET (0x12)``` - command code
```line (1 byte)``` - line number
```level (1 byte)``` - level value: 0 (low) or 1 (high)

### LISTEN (0x13)

Subscribe for level change events on output ```line```. Each edge event is reported to POLL command.

```LISTEN (0x13)``` - command code
```line (1 byte)``` - line number
```active level (1 byte)``` - line active level: 0 (low) or 1 (high)

##### Response:
```ACK```

### POLL (0x14)

Get pending events.

##### Response:

- ```ACK``` when there is no pending events
- Raising or falling edge event occured:
```LISTEN (0x13)``` - command code
```line (1 byte)``` - line number
```level (1 byte)``` - line current level: 0 (low) or 1 (high)

### LOGS (0x15)

Get system logs file. After sending ACK response transport is switched to file send mode (for UART XModem-1K).

```LOGS (0x15)``` - command code

##### Response:
```ACK```

### UPDATE (0x16)

Update firmware. After sending ACK response transport is switched to file receive mode (for UART XModem-1K).

```UPDATE (0x16)``` - command code

##### Response:
```ACK```

### VERSION (0x17)

Get firmware version in ```major```.```minor``` format.

```VERSION (0x17)``` - command code

##### Response:
```VERSION (0x17)``` - command code
```major (1 byte)``` - major version
```minor (1 byte)``` - minor version
