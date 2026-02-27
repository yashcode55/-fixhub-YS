# FIX HUB – System Architecture Document

## 1. Overview

FIX HUB is a high-performance FIX protocol bridge designed to:

- Support ~500 concurrent FIX sessions
- Act as both FIX Acceptor and Initiator
- Provide message enrichment capability
- Perform grammar-based FIX parsing and rebuilding
- Route messages dynamically between sessions
- Operate as a FIX bridge between clients and brokers

This system is designed as a scalable FIX connectivity infrastructure component.

---

## 2. High-Level Architecture

Client OMS
    ↓
[ FIX Acceptor Layer ]
    ↓
[ FIX Parser / Grammar Engine ]
    ↓
[ Enrichment Engine ]
    ↓
[ Routing Engine ]
    ↓
[ FIX Builder ]
    ↓
[ FIX Initiator Layer ]
    ↓
Broker Gateway

Bidirectional communication supported.

---

## 3. Core Components

### 3.1 FIX Transport Layer
Technology:
- QuickFIX/J
- Java NIO
- Thread Pool Management

Responsibilities:
- Session Management
- Logon/Logout
- Heartbeats
- Sequence Handling
- Resend Requests
- Gap Fill Handling

---

### 3.2 Grammar Engine

Purpose:
- Convert FIX string to internal structured object
- Convert internal object back to FIX string

Process:
FIX String → Tag-Value Parsing → Internal Model → Processing
Internal Model → Tag Assembly → FIX String → Send

Implementation:
- Custom FIX message wrapper
- Efficient tag parsing
- Validation layer

---

### 3.3 Enrichment Engine

Optional enrichment:
- Add default tags
- Validate required tags
- Normalize price format
- Add timestamps
- Add routing metadata

Must be configurable per session.

---

### 3.4 Routing Engine

Responsibilities:
- Maintain session mapping (Client ↔ Broker)
- Route based on:
  - SenderCompID
  - TargetCompID
  - Message Type (35)
- Support failover logic
- Reject if target session unavailable

Must be thread-safe.

---

### 3.5 Session Registry

ConcurrentHashMap<SessionID, SessionContext>

SessionContext:
- Session Type (CLIENT / BROKER)
- Status
- Counterparty mapping
- Heartbeat interval
- Login credentials
- Connection state

---

### 3.6 Persistence Layer

PostgreSQL Tables:

fix_session_config
- id
- session_type
- sender_comp_id
- target_comp_id
- host
- port
- username
- password
- heartbeat_interval
- enabled

fix_session_mapping
- id
- client_session_id
- broker_session_id
- active

---

## 4. Scalability Design

Target: 500 FIX Sessions

Requirements:
- Non-blocking IO
- Thread pool tuning
- JVM memory tuning
- GC optimization (G1GC)
- Session isolation
- Log optimization

---

## 5. Failure Handling

Cases:

1. Broker Disconnect
   - Notify client
   - Pause routing

2. Client Sends Order When Broker Down
   - Reject with BusinessReject

3. Sequence Mismatch
   - Allow resend
   - Maintain consistency

4. Duplicate Execution Reports
   - Detect and log

---

## 6. Security

- TLS support
- Password validation
- IP whitelisting
- Encrypted credentials in DB

---

## 7. Deployment

- Java 17
- Maven build
- Local deployment initially
- Future containerization

---

## 8. Future Roadmap

- FIX 4.4 support
- Multi-tenant
- Web-based monitoring
- High Availability cluster
- Redis-backed session registry
- Metrics (Prometheus)
