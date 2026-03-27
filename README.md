
Document Management System
Technical Documentation & Developer Guide
Spring Boot • MongoDB GridFS • AWS S3 • Tesseract OCR • PostgreSQL



1. Project Overview
The Document Management System (DMS) is a Spring Boot REST API that enables users to securely upload, retrieve, and manage documents. It provides automated OCR text extraction using Tesseract and OpenCV, file deduplication via SHA-256 hashing, and a background migration pipeline that automatically moves older files from local MongoDB GridFS storage to AWS S3 for cost-efficient long-term retention.
1.1 Key Capabilities
•	Secure document upload with type and size validation (PDF, PNG, JPEG — 10 MB limit)
•	Content-aware deduplication using SHA-256 file hashing per user
•	Dual storage strategy: MongoDB GridFS for hot storage, AWS S3 for cold/archived storage
•	Asynchronous OCR text extraction using Tesseract with OpenCV image preprocessing
•	Scheduled nightly migration of documents older than 30 days from GridFS to S3
•	Structured metadata persistence in PostgreSQL via Spring Data JPA
•	Global exception handling with consistent RFC-style error responses

1.2 Technology Stack
Component	Technology / Library
Backend Framework	Spring Boot 3.x (Java)
REST API	Spring Web MVC
Persistence (Metadata)	Spring Data JPA + PostgreSQL
Hot Storage (Files)	MongoDB GridFS via Spring Data MongoDB
Cold Storage (Files)	AWS S3 via AWS SDK v2
OCR Engine	Tess4J (Tesseract Java wrapper)
Image Processing	OpenCV (nu.pattern)
PDF Rendering	Apache PDFBox
Object Mapping	ModelMapper
Async Execution	Spring @Async with thread pool
Scheduled Tasks	Spring @Scheduled (cron)
