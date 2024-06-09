# CueCoLab

Welcome to [CueCoLab](http://cuecolab.com), a platform designed to streamline collaboration between content creators and their editors by providing a shared workspace where efficiency, security, and convenience are the cornerstones.

![CueCoLab Interface](link_to_image_here)  <!-- If you have an image, replace 'link_to_image_here' with the URL to the image. -->

## Table of Contents
- [Introduction](#introduction)
- [Problems Faced by Content Creators](#problems-faced-by-content-creators)
- [Solutions Provided by CueCoLab](#solutions-provided-by-cuecolab)
- [Benefits of Using CueCoLab](#benefits-of-using-cuecolab)
- [Development Journey](#development-journey)
- [Technical Challenges](#technical-challenges)
- [Technology Stack](#technology-stack)
- [Architectures Used](#architectures-used)
- [Join the Beta](#join-the-beta)

## Introduction
I coded 15 hours/day for 3 months and developed a SaaS product, CueCoLab, to address specific challenges in the content creation industry. Check it out here: [CueCoLab](https://cuecolab.com).

## Problems Faced by Content Creators
Content creators, especially YouTube channel owners, encounter several issues:
- **Connectivity Issues:** Frequent traveling often leads to poor internet connectivity, making it difficult to download and upload large video files.
- **Security Concerns:** Reluctance to provide direct editing access to others due to security risks.
- **Inefficiency:** The need to manually download and upload videos is time-consuming and cumbersome.

## Solutions Provided by CueCoLab
CueCoLab addresses these challenges through:
- **Intermediate Platform:** Editors can upload videos directly to CueCoLab Room.
- **Video Pre-upload Playback:** Channel owners can review videos on CueCoLab before final upload.
- **YouTube Integration:** Direct publishing to YouTube via CueCoLab using YouTube’s API.
- **Control and Security:** Channel owner retains full control with approval required for each video.
- **Notification System:** Upcoming feature to approve uploads remotely without direct file handling.

## Benefits of Using CueCoLab
Using CueCoLab offers significant advantages:
- **Efficiency:** Drastically cuts down the time needed for managing video uploads.
- **Security:** Ensures that uploads are fully authorized.
- **Convenience:** Facilitates remote management of content, ideal for travel and poor connectivity.

## Development Journey
Starting on March 11th, 2024, my journey began with learning front-end development from scratch:
- **Skill Acquisition:** From JavaScript to advanced frameworks like Next.js with TypeScript.
- **Design and Prototyping:** Initial sketches to final design iterations on Canva.
- **Back-end and Front-end Development:** Building a robust back-end using Spring Boot and crafting an interactive front-end.

## Technical Challenges
- **Video Transcoding and Streaming:** Implemented using ffmpeg and AWS services for cost-effective, scalable solutions.
- **Secured HLS Streaming:** Achieved with CloudFront's signed URLs after overcoming challenges with signed cookies.
- **Direct YouTube Uploads:** Utilized YouTube Data API to facilitate uploads directly from CueCoLab.

## Technology Stack
**Frontend:**
1. Next.js over React with TypeScript
2. Tailwind CSS and MaterialUI

**Backend:**
1. Spring Boot
2. Spring Security

**Cloud (AWS):**
1. CloudFront
2. ECS (Fargate)
3. EventBridge
4. SQS
5. S3
6. Lambda

## Architectures Used
I implemented several AWS architectures involving ECS Fargate, EventBridge, and more. <!-- If you have images, you can insert them here. -->

## Join the Beta
Currently, CueCoLab is in its testing phase with availability for up to 100 users. Interested? Provide your Gmail ID associated with your YouTube channel to join the beta.

---

#buildinpublic #SaaS

