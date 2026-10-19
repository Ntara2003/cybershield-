# CyberShield SA — Android MVP

Developed under **Ntara Enterprises (Pty) Ltd**
**Tagline:** "Cybersecurity Awareness. Safer Digital Habits."

CyberShield SA is a mobile cybersecurity awareness application specifically designed for the South African market—empowering ordinary citizens, students, freelancers, and small business owners (SMEs) to recognize emerging digital threats, develop resilient security habits, and report suspicious activities.

---

## 🛡️ Core Features in this MVP

1. **Secure Authentication & Session Management**:
   - Registration, login, session persistence, display name management, and POPIA-compliant account deletion.
   - Credentials hashed using SHA-256 with cryptographic salt; zero plaintext storage.
   - Clear terms acceptance and privacy disclosure.

2. **Personalized Home Dashboard**:
   - Welcome banner personalized to user profile.
   - Rotatable Daily Cybersecurity Tip with concrete action steps.
   - Live Security Readiness metrics: Overall Progress, Lessons Completed, Mastered Courses, Latest Quiz Score, and SME Checklist %.
   - Quick-action shortcuts directly into the Academy, SME Checklist, and Scam Report form.
   - **"Stay Alert"**: Curated, verified live threat intelligence on active South African scams (SARS eFiling refund phishing, Capitec/FNB SMS fraud, WhatsApp impersonation).

3. **CyberShield Academy**:
   - 10 Foundational course categories covering email phishing, WhatsApp & SMS scams, fake Proof of Payment (PoP), password management, MFA & SIM swaps, social engineering, web browsing safety, POPIA & privacy rights, and SME security basics.
   - In-depth interactive lessons with South African scenarios, attack mechanisms, visual red flags, safe defense habits, and key takeaways.

4. **Interactive Quiz Engine**:
   - Multiple choice and true/false questions.
   - Answer selection with accidental submission prevention.
   - Instant feedback with comprehensive explanations of why answers are correct or incorrect.
   - Results summary screen featuring an **80% passing standard**, breakdown of score, course completion status, and retry functionality.

5. **SME Security Self-Assessment Checklist**:
   - 15 practical controls across 5 categories: Account Security, Devices & Software, Data Protection, Employee Awareness, and Incident Readiness.
   - Interactive checkbox tracking with persistent progress.
   - Explanations and actionable recommendations for every item.
   - Prominent disclaimer highlighting that the checklist is an educational self-assessment, not a legal certification or formal penetration audit.

6. **Confidential Scam Incident Reporting**:
   - Comprehensive reporting form capturing scam category, communication channel (Email, SMS, WhatsApp, Phone call, Website, etc.), detailed incident narrative, and approximate date.
   - Optional screenshot attachment simulator stored in a private local vault.
   - Prominent privacy warning against entering passwords, banking PINs, or ID numbers.
   - Status tracking ("Submitted", "Under Review", "Resolved") in the user's "My Reports" tab.
   - Directory of official South African reporting channels (SAPS Cybercrime 10111, SABRIC, Information Regulator).

7. **Profile & Legal Compliance (POPIA)**:
   - Account overview, membership date, and activity statistics.
   - Edit display name modal.
   - Secure account deletion workflow with legal explanation of statutory retention limits under South African law.
   - Complete Privacy Notice and Terms dialog.

---

## 🏗️ Technical Architecture

- **Platform:** Android Native (API 24+)
- **Language:** Kotlin 2.2+
- **UI Framework:** Jetpack Compose with Material Design 3 (M3)
- **Local Persistence:** Room Database (`CyberShieldDatabase`) with Flow and suspend functions
- **Design System:**
  - Primary Background: Dark Navy Blue (`#0A1128`)
  - Surface Containers: Navy Surface (`#101F3C`) & Elevated Surface (`#1E3560`)
  - Accent Color: Radiant Cyan (`#00E5FF`) & Teal (`#00B4D8`)
  - Status Indicators: Emerald Green (`#10B981`), Amber (`#F59E0B`), Crimson (`#EF4444`)
- **Backend / Cloud Option:** Supabase SQL migration files provided in `/supabase/schema.sql` and `/supabase/seed.sql` with full Row Level Security (RLS) policies.

---

## 🚀 Building & Running the Application

### Prerequisites
- Android Studio Ladybug / Meerkat or Gradle command-line toolchain
- Android SDK 36 (minSdk 24)
- JDK 17 or JDK 21

### Local Compilation
To compile and assemble the debug APK:
```bash
gradle :app:assembleDebug
```

To run JVM unit and Robolectric tests:
```bash
gradle :app:testDebugUnitTest
```

---

## 🗄️ Supabase Cloud Backend Integration

To deploy this backend to Supabase:
1. Create a free project at [supabase.com](https://supabase.com).
2. Open the **SQL Editor** in the Supabase Dashboard.
3. Paste the contents of `/supabase/schema.sql` and run the script to create tables, indexes, and RLS policies.
4. Run `/supabase/seed.sql` to populate courses and SME checklist items.
5. In Supabase Storage, create a private bucket named `scam-evidence` with access restricted to authenticated owners.

---

## 🧪 Manual QA & Testing Checklist

- [x] **Registration Validation:** Rejects empty names, invalid email formats, passwords shorter than 6 characters, and unaccepted terms.
- [x] **Authentication Session:** Logging in sets user session; user name appears on Home and Profile screens; logging out clears session.
- [x] **Academy Navigation:** Tapping any course opens lessons and the quiz launcher; completed lessons show a green checkmark.
- [x] **Quiz Engine:** Selecting an option highlights the card; clicking "Lock In Answer" reveals the explanation with correct/incorrect feedback; scoring 80%+ marks the course as passed.
- [x] **SME Checklist:** Tapping checkboxes updates the completion percentage bar in real time and persists across restarts.
- [x] **Scam Reporting:** Form verifies at least 15 characters of description; submitted reports immediately appear under "My Reports" with "Submitted" status.
- [x] **Profile & Account Deletion:** Changing display name updates the user header; requesting account deletion wipes local progress and logs the user out.

---

## 📄 License & Attribution

Copyright © 2026 Ntara Enterprises (Pty) Ltd. All rights reserved.
Developed for public cybersecurity awareness in South Africa.
