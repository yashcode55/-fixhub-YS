# FIX HUB (YS FIX Bridge)

## Overview

FIX HUB is a high-performance FIX protocol bridge built in Java.

It supports:
- Up to 500 concurrent FIX sessions
- Dynamic session creation
- Message enrichment
- Bidirectional routing
- Grammar-based parsing and rebuilding
- FIX 4.2 (Phase 1)
- FIX 4.4 (Future Phase)

This system acts as an enterprise-grade FIX connectivity layer between client OMS systems and broker gateways.

---

## Architecture

Client → FIX HUB → Broker

Flow:
Client Message
→ Parse FIX Grammar
→ Convert to Internal Model
→ Enrichment (Optional)
→ Routing Decision
→ Rebuild FIX Message
→ Send to Target Session

Reverse path supported.

---

## Technology Stack

Backend:
- Java 17
- QuickFIX/J
- Maven
- PostgreSQL

Frontend:
- JavaFX (Desktop Monitoring & Control)

Infrastructure:
- Multi-threaded architecture
- ConcurrentHashMap Session Registry
- Thread Pool Executor
- G1GC

---

## Core Modules

- fix-core (QuickFIX/J integration)
- grammar-engine
- enrichment-engine
- routing-engine
- session-management
- persistence-layer
- ui-layer (JavaFX)

---

## Scalability Goal

- 500 concurrent FIX sessions
- Low latency routing
- High throughput
- Production-grade logging

---

## Setup Instructions

1. Install Java 17
2. Install Maven
3. Install PostgreSQL
4. Clone repository:
   git clone https://github.com/yashcode55/-fixhub-YS.git
5. Run:
   mvn clean install
6. Launch main application

---

## Author

Yash Akolkar
FIX Specialist | Java Developer
