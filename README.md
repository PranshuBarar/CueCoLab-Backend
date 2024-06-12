# CueCoLab

Welcome to [CueCoLab](http://cuecolab.com), a platform designed to streamline collaboration between content creators and their editors by providing a shared workspace where efficiency, security, and convenience are the cornerstones.

![Screenshot from 2024-06-12 18-10-17](https://github.com/PranshuBarar/CueCoLab-Frontend/assets/117909106/33a42d74-9df0-4399-b280-8d760b36fbd0)
![CueCoLab Interface](https://github.com/PranshuBarar/CueCoLab-Frontend/assets/117909106/e5843a8d-ad69-419e-9e89-16fed44195c3)
![Screenshot from 2024-06-12 18-08-52](https://github.com/PranshuBarar/CueCoLab-Frontend/assets/117909106/0ae13dfe-e231-4a6b-bac7-de2acd5d78fb)
<!-- If you have an image, replace 'link_to_image_here' with the URL to the image. -->

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

## Problems Content Creators Face
Content creators, especially YouTube channel owners, encounter several issues:
- **Connectivity Issues:** A YouTube channel owner frequently travels and faces challenges with internet connectivity, which hampers the ability to download and upload large video files edited by other people.
- **Security Concerns:** The channel owner is reluctant to give direct editing access to others on their YouTube channel due to security concerns.
- **Inefficiency:** The current process involves manual effort where the channel owner must download each video and then upload it to YouTube, which is inefficient and inconvenient.

## How I solved the problem by building CueCoLab
- **Intermediate Platform:** CueCoLab serves as an intermediary platform where video editors can upload finished videos directly in the CueCoLab Room.
- **Video Pre-upload Playback:** Before finalizing the upload, the channel owner can play and review the video directly on CueCoLab, ensuring it meets their standards and preferences.
- **YouTube Integration:** CueCoLab is integrated with YouTube, allowing videos uploaded in the CueCoLab Room to be directly published to the channel. CueCoLab utilizes YouTube’s API for publishing videos to channel, ensuring that the process is as seamless as possible.
- **Control and Security:** The channel owner retains full control over the publication process. Each video upload by an editor requires the channel owner’s approval before it goes live on YouTube.
- **Notification System:** (This is not implemented yet but in the pipeline) The approval process is streamlined through notifications (like WhatsApp or Slack), allowing the channel owner to approve uploads remotely without needing to handle the video files directly.
- **Storage and Handling:** Videos are temporarily stored on CueCoLab Room (shared workspace), reducing the need for the channel owner to download large files.
  
## Benefits of Using CueCoLab
Using CueCoLab offers significant advantages:
- **Efficiency:** Significantly reduces the time and effort required by the channel owner to manage video uploads.
- **Security:** Ensures that the channel owner has final say over what content gets published, preventing unauthorized uploads.
- **Convenience:** Allows the channel owner to approve video uploads from anywhere, addressing the inconvenience of traveling and poor internet access.

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
I implemented several AWS architectures involving ECS Fargate, EventBridge, and more.

### AWS Architecture for Video Transcoding
![cuecolab architecture finalized22 drawio](https://github.com/PranshuBarar/CueCoLab-Frontend/assets/117909106/fda21a46-7933-4b2f-af54-a8a4f0b26572)

### AWS Architecture for Uploading Video From CueCoLab Room To YouTube Channel
![VideoUploadToChannelDiagram](https://github.com/PranshuBarar/CueCoLab-Frontend/assets/117909106/df805bf0-0900-4ecc-910a-fe8daad564f1)



## Join the Beta
Currently, CueCoLab's direct video publish feature (from CueCoLab Room to YouTube Channel) is in its testing phase with availability for up to 100 users. Interested? Provide your Gmail ID associated with your YouTube channel to join the beta.

You can contact me at support@cuecolab.com or pranshubarar1851996@gmail.com

---

#buildinpublic #SaaS

