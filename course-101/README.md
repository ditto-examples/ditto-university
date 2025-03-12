# Course 101: Build Your First Ditto-Powered App 🚀

The Introduction to the Ditto SDK course guides you through the foundational skills and knowledge you need to get started with the Ditto SDK. This includes how to sign up and create your first Ditto app on the Ditto Portal, how to conduct simple CRUD operations, syncing data between small peers, and syncing to the big peer in the cloud.  You'll experience a common real-world scenario: transforming a prototype into a production-ready application with robust data management capabilities.

> [!NOTE] 
>This is a proof of concept for the Ditto University.  The concepts and code provided is not production ready and is only meant to be used as a reference for the POC of Ditto University learning modules. 

## Overview

### 🎯 The Scenario

You've just joined a development team for the company Acmezon that recently demonstrated a Project Management app to company leadership. The app demo was a hit - the UI was polished, the interactions were smooth, and leadership was ready to ship it immediately. There was just one catch: the entire app was running on mock data.

The prototype lacks several critical features modern users expect:
- No offline support - data disappears when the app closes
- No data persistence - changes aren't saved between sessions
- No synchronization - changes don't sync between devices
- No real-time updates - users don't see each other's changes

Rather than rebuilding from scratch, the team has decided to integrate the Ditto SDK to add these essential features. Your mission is to transform this prototype into a production-ready application with robust offline-first capabilities and peer-to-peer synchronization, all while maintaining the existing user experience.

## 🎓 Learning Objectives

Through this module, you'll learn how to:
- What is the Ditto Portal is and how it works
- Setup a Ditto Portal account and create an app
- Replace mock data with a real Ditto data store
- Implement offline-first data persistence
- Update the app to perform CRUD operations
- Enable real-time updates across devices (small peers)
- Sync data to the Big Peer in the Cloud

## 📖 Table of Contents

1. [Create Your Portal Account](lesson_1/README.md)
  - Register Your Portal Account

2. [Setting Up Your Development Environment](lesson_2/README.md)
  - Learn about the Ditto SDK
  - Cloning the Code
  - Running the App with Mock Data
  - Providing Authentication Credentials
  - Setting up the Ditto Identity

3. [Updating the initial Data with Ditto](lesson_3/README.md)
  - Collections
  - Understanding the Code Structure
  - Review of the Data Model 
  - DQL INSERT and SELECT Statement 
  - Register Observers
  - Update the App to use the Ditto Database

4. [DQL INSERT, UPDATE, and SOFT DELETE](lesson_4/README.md)
  - Learn about DQL UPDATE and SOFT DELETE
  - Update the App to use DQL INSERT, UPDATE, and SOFT DELETE
  - View changes on a device

5. [Syncing Data Across Devices using Small Peers](lesson_5/README.md)
  - Learn about Small Peers
  - Test the App with Small Peers

6. [Syncing Data Across Devices using the Big Peer](lesson_6/README.md)
  - Learn about the Big Peer
  - Learn the DQL Editor in the Ditto Portal
  - Test the App with Big Peer syncing changes from the Big Peer to Small Peers

## 🚀 Ready to Begin?

Head to [Lesson 1 - Create Your Account](lesson-1/README.md) to get started! 


