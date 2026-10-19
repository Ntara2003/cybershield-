package com.example.data.local

import com.example.data.model.DailyTip
import com.example.data.model.ThreatAlert

object SeedData {

    val courses = listOf(
        CourseEntity(
            id = "course-1",
            title = "Phishing & Email Scams",
            description = "Spot fraudulent emails, spoofed sender domains, and fake invoices targeting South Africans.",
            category = "Email Security",
            difficulty = "Beginner",
            estimatedMinutes = 15,
            lessonsCount = 2,
            iconName = "email",
            position = 1
        ),
        CourseEntity(
            id = "course-2",
            title = "WhatsApp & SMS Scams",
            description = "Defend against banking SMS lures, fake parcel notices, and 'Hi Mom' WhatsApp impersonation scams.",
            category = "Mobile Messaging",
            difficulty = "Beginner",
            estimatedMinutes = 12,
            lessonsCount = 2,
            iconName = "chat",
            position = 2
        ),
        CourseEntity(
            id = "course-3",
            title = "Online Shopping & Payment Scams",
            description = "Avoid fake proof of payment (PoP) tricks, fraudulent Facebook Marketplace sellers, and escrow scams.",
            category = "E-Commerce",
            difficulty = "Beginner",
            estimatedMinutes = 15,
            lessonsCount = 1,
            iconName = "shopping_cart",
            position = 3
        ),
        CourseEntity(
            id = "course-4",
            title = "Password Security & Managers",
            description = "Create unbreakable passphrases, use password vaults, and check if your credentials leaked in a breach.",
            category = "Access Control",
            difficulty = "Beginner",
            estimatedMinutes = 10,
            lessonsCount = 1,
            iconName = "lock",
            position = 4
        ),
        CourseEntity(
            id = "course-5",
            title = "Multi-Factor Authentication (MFA)",
            description = "Why passwords alone fail, authenticator apps vs SMS OTPs, and how to defend against SIM-swap attacks.",
            category = "Access Control",
            difficulty = "Intermediate",
            estimatedMinutes = 12,
            lessonsCount = 1,
            iconName = "security",
            position = 5
        ),
        CourseEntity(
            id = "course-6",
            title = "Social Engineering & Pretexting",
            description = "Understand psychological manipulation, urgent phone calls pretending to be bank fraud departments, and CEO fraud.",
            category = "Human Defense",
            difficulty = "Intermediate",
            estimatedMinutes = 15,
            lessonsCount = 1,
            iconName = "psychology",
            position = 6
        ),
        CourseEntity(
            id = "course-7",
            title = "Safe Browsing & Malicious Links",
            description = "Learn how to inspect URL domain names, detect lookalike websites (typosquatting), and prevent dangerous APK downloads.",
            category = "Web Safety",
            difficulty = "Beginner",
            estimatedMinutes = 10,
            lessonsCount = 1,
            iconName = "language",
            position = 7
        ),
        CourseEntity(
            id = "course-8",
            title = "Protecting Personal Information",
            description = "Safeguard your South African ID number, banking cards, proof of residence, and identity theft countermeasures.",
            category = "Privacy",
            difficulty = "Beginner",
            estimatedMinutes = 12,
            lessonsCount = 1,
            iconName = "badge",
            position = 8
        ),
        CourseEntity(
            id = "course-9",
            title = "Cybersecurity Basics for Small Businesses",
            description = "Essential digital hygiene for South African SMEs: guest WiFi segregation, access control, and 3-2-1 backups.",
            category = "Business Protection",
            difficulty = "Intermediate",
            estimatedMinutes = 20,
            lessonsCount = 1,
            iconName = "business",
            position = 9
        ),
        CourseEntity(
            id = "course-10",
            title = "Introduction to POPIA & Personal Data",
            description = "The Protection of Personal Information Act (POPIA): lawful conditions, customer consent, and breach notification duties.",
            category = "Compliance & Law",
            difficulty = "Intermediate",
            estimatedMinutes = 18,
            lessonsCount = 1,
            iconName = "gavel",
            position = 10
        )
    )

    val lessons = listOf(
        LessonEntity(
            id = "lesson-1-1",
            courseId = "course-1",
            title = "Identifying Fake SARS Tax Refund Notices",
            summary = "How scammers impersonate the South African Revenue Service during filing season.",
            scenario = "Thabo receives an urgent email appearing to be from 'SARS eFiling Notification' stating: 'You have an outstanding tax refund of R8,420.00. Click the secure link below to verify your banking details before the refund expires in 48 hours.' The sender address is actually 'support@sars-efiling-portal-sa.online'.",
            howItWorks = "Scammers create cloned login portals that look identical to the real SARS eFiling site. When you enter your login credentials, password, and bank details, the attackers capture them immediately to drain your account or commit identity fraud.",
            redFlags = listOf(
                "Sender address does not end with '@sars.gov.za'",
                "Artificial urgency ('expires in 48 hours') pressuring you to react without thinking",
                "Direct link asking for bank account login details or debit card CVV",
                "Generic greeting such as 'Dear Taxpayer' rather than Thabo's registered tax reference number"
            ),
            safeHabits = listOf(
                "Never click links in tax refund or audit emails",
                "Always open your browser directly and type 'www.sarsefiling.co.za' or use the official SARS MobiApp",
                "Remember that SARS will never ask for your banking password, ATM PIN, or card CVV via email or SMS",
                "Forward suspicious emails to 'phishing@sars.gov.za'"
            ),
            keyTakeaways = "Official tax authorities never send direct login links for refunds. Always navigate independently to verified government portals.",
            position = 1
        ),
        LessonEntity(
            id = "lesson-1-2",
            courseId = "course-1",
            title = "The Spoofed Invoice & Banking Details Scam",
            summary = "Business email compromise (BEC) intercepting supplier bank account numbers.",
            scenario = "A Cape Town plumbing business receives an email from regular supplier 'Atlas Pipes' requesting payment for last week's invoice. The email includes an invoice PDF saying: 'Please note our banking account has been changed from FNB to Nedbank. Find the updated account details below.'",
            howItWorks = "Cybercriminals either hack into the supplier's email mailbox or register a visually indistinguishable lookalike domain (e.g. '@atlas-pipes.co.za' instead of '@atlaspipes.co.za'). They monitor email threads and swap the banking details on legitimate invoice documents.",
            redFlags = listOf(
                "Notice of unexpected change in supplier banking details",
                "Subtle alteration in the email domain name (hyphens, transposed letters)",
                "Sender insists on immediate EFT payment before physical goods dispatch",
                "Attached PDF shows different bank logo formatting or blurry bank stamp"
            ),
            safeHabits = listOf(
                "Enforce a mandatory dual-control verification policy for ANY change to supplier banking details",
                "Always call the known, trusted financial contact at the supplier using a pre-saved phone number (never the phone number inside the suspicious email)",
                "Ask your bank to verify the account holder name against the registered account before executing large transfers",
                "Enable DMARC, DKIM, and SPF email authentication records on your company domain"
            ),
            keyTakeaways = "Never update vendor banking details based solely on an email request. Always perform out-of-band phone verification using a verified number.",
            position = 2
        ),
        LessonEntity(
            id = "lesson-2-1",
            courseId = "course-2",
            title = "The 'Hi Mom / Hi Dad' WhatsApp Impersonation Scam",
            summary = "Emotional manipulation tricking parents into emergency cash transfers.",
            scenario = "Nomvula receives a WhatsApp message from an unknown number with her daughter Lerato's profile photo: 'Hi Mom, I dropped my phone in the water and my speaker is broken. This is my temporary number. I have an urgent bill of R2,800 due for university textbook fees by 5 PM. Can you please EFT it to my friend's Capitec account? I will pay you back tomorrow!'",
            howItWorks = "Scammers scrape photos and names from social media platforms. They exploit the parent's protective instinct, inventing an excuse why they cannot talk on a phone call ('broken mic'), and demand instant payment to a mule account.",
            redFlags = listOf(
                "Sudden message from an unknown number claiming to be a close relative with a 'lost/broken phone'",
                "Immediate request for money, gift vouchers, or payment to a third-party account",
                "Refusal or inability to take a voice call or send a voice note",
                "Extreme emotional pressure to act before verifying"
            ),
            safeHabits = listOf(
                "Immediately call your child's original, known phone number to verify",
                "Ask a personal security question only the real person would know (e.g. 'What was the name of our first family dog?')",
                "Demand a real-time voice note addressing you by your personal family nickname",
                "Never transfer funds to unfamiliar bank accounts without verbal confirmation"
            ),
            keyTakeaways = "Whenever a relative texts from a new number requesting urgent money, stop and call their old number or ask a shared secret question before sending anything.",
            position = 1
        ),
        LessonEntity(
            id = "lesson-2-2",
            courseId = "course-2",
            title = "Fake Courier Delivery & Customs SMS (Smishing)",
            summary = "Phishing links disguised as SAPO, DHL, or Aramex delivery notifications.",
            scenario = "Sipho receives an SMS: 'SAPost: Your parcel ZA-99214 could not be delivered due to an incomplete delivery address and unpaid clearance fee of R28.50. Update details now at: https://sapo-tracking-za.info/delivery'.",
            howItWorks = "Scammers blast millions of SMS messages hoping recipients are expecting an online order (e.g. Shein, Takealot, Temu). The link leads to a phishing page requesting credit card numbers, expiry dates, CVVs, and even 3D Secure OTPs.",
            redFlags = listOf(
                "SMS sent from an ordinary mobile cellular number (e.g. +27 72 ...) rather than an official courier shortcode",
                "Odd domain name that is not the official courier website (e.g. 'sapo-tracking-za.info')",
                "Request to pay a tiny fee (R20 - R35) just to harvest card details",
                "Vague parcel tracking number that you never received from your original merchant"
            ),
            safeHabits = listOf(
                "Never click links in unsolicited parcel delivery text messages",
                "Check your order status directly inside the Takealot, Shein, or courier mobile app you originally purchased through",
                "Be suspicious of any delivery notification asking for debit card details or banking logins",
                "Block and report the sender number as spam on your mobile phone"
            ),
            keyTakeaways = "Couriers will not ask you to enter credit card details on random third-party web domains to resolve address issues.",
            position = 2
        ),
        LessonEntity(
            id = "lesson-3-1",
            courseId = "course-3",
            title = "Fake Proof of Payment (PoP) & Marketplace Scams",
            summary = "Selling items on Facebook Marketplace or Gumtree without falling for forged bank receipts.",
            scenario = "Kagiso advertises a gaming laptop for R12,000 on Facebook Marketplace. A buyer contacts him eagerly, refuses to meet in person, and sends an Uber courier to collect the laptop. Minutes before arrival, the buyer sends a PDF and SMS showing a Capitec payment notification for R12,000.",
            howItWorks = "Scammers use online PDF templates or spoofed SMS sender IDs to create fraudulent Proof of Payment receipts. Kagiso hands the laptop to the courier, only to discover hours later that no funds ever arrived in his bank account.",
            redFlags = listOf(
                "Buyer is overly eager, does not negotiate price, and insists on sending a driver/courier immediately",
                "Proof of Payment is sent via SMS or WhatsApp screenshot instead of direct bank clearance",
                "Funds appear as 'Pending' or 'Uncleared Cheque' rather than cleared available balance",
                "Buyer claims payment was made from a different bank and urges immediate handover before clearing"
            ),
            safeHabits = listOf(
                "Never release goods based on a screenshot or email Proof of Payment",
                "Log into your official banking app and verify the money is in your AVAILABLE balance, not just pending or ledger balance",
                "For high-value items, insist on real-time immediate clearing (e.g. PayShap, RTC) or meet inside a secure, public bank branch",
                "Beware of cheque deposits that can reverse after several business days"
            ),
            keyTakeaways = "A Proof of Payment document can be faked in 60 seconds. Only trust the cleared available balance in your official banking app.",
            position = 1
        ),
        LessonEntity(
            id = "lesson-4-1",
            courseId = "course-4",
            title = "Building Strong Passphrases & Using Password Vaults",
            summary = "Why 'P@ssw0rd123' fails instantly and how 4 random words protect your life.",
            scenario = "Zanele uses the same password ('Zanele2022!') across her Gmail, online banking, Takealot account, and work email. When a small fitness app she joined suffers a data breach, cybercriminals use automated tools to test her credentials against all other major services.",
            howItWorks = "Credential stuffing attacks use bots to replay breached usernames and passwords against hundreds of popular websites. If you reuse passwords, one single compromise exposes your entire digital identity.",
            redFlags = listOf(
                "Using the same root password with minor variations (e.g. Zanele1, Zanele2)",
                "Using personal info like birth years, pet names, or street names",
                "Writing passwords down in plaintext notebooks or unprotected mobile notes apps",
                "Ignoring notices that your email was found in a known data breach"
            ),
            safeHabits = listOf(
                "Adopt passphrases made of 4 or 5 random, memorable words (e.g. 'koeksister-mountain-solar-bicycle')",
                "Use a reputable password manager (e.g. Bitwarden, 1Password) to generate unique 16+ character passwords for every account",
                "Check 'haveibeenpwned.com' periodically to see if your email was involved in a breach",
                "Change compromised passwords immediately when notified"
            ),
            keyTakeaways = "Length beats complexity. A 4-word passphrase is easy to remember and exponentially harder for brute-force algorithms to crack.",
            position = 1
        ),
        LessonEntity(
            id = "lesson-5-1",
            courseId = "course-5",
            title = "MFA Best Practices & Defending Against SIM Swaps",
            summary = "Moving beyond SMS OTPs to authenticator apps and hardware keys.",
            scenario = "David has SMS two-factor authentication enabled on his cryptocurrency and email accounts. An attacker bribes an employee at a telecommunications store to perform an unauthorized SIM swap. David suddenly loses mobile network signal ('No Service'), and within minutes, the attacker requests password resets.",
            howItWorks = "In a SIM swap scam, the attacker transfers your phone number to a SIM card in their possession. They then intercept all incoming SMS OTP codes and password reset tokens sent by your bank or email provider.",
            redFlags = listOf(
                "Sudden loss of cellular reception ('No Service' or 'Emergency Calls Only') in an area with normally strong coverage",
                "Receiving unsolicited carrier notifications about a SIM change or PAC code request",
                "Unexplained phone calls asking you to turn off your phone for network upgrades"
            ),
            safeHabits = listOf(
                "Switch from SMS-based verification to Time-based Authenticator Apps (Google Authenticator, Microsoft Authenticator, Aegis)",
                "Set a strong account PIN with your mobile network provider (Vodacom, MTN, Telkom, Cell C)",
                "If your phone unexpectedly loses cellular connectivity, immediately contact your network provider from another phone to check for a SIM swap",
                "Never share an OTP with anyone over the phone, even if they claim to work for your bank's fraud team"
            ),
            keyTakeaways = "Authenticator apps are immune to SIM swaps because keys reside strictly on your physical device, not over the cellular network.",
            position = 1
        ),
        LessonEntity(
            id = "lesson-9-1",
            courseId = "course-9",
            title = "Cyber Resilience for South African Small Businesses",
            summary = "Practical baseline controls: separating guest networks, offboarding staff, and 3-2-1 backups.",
            scenario = "A boutique consulting firm in Rosebank has 12 employees. All staff connect to the same router WiFi as visiting clients. One staff member leaves the company on bad terms, but still has access to shared Google Drive folders and the accounting portal.",
            howItWorks = "Small businesses often lack formal access revocation and network segmentation. Shared administrative passwords and unprotected shared drives leave business IP vulnerable to insider threats and lateral ransomware propagation.",
            redFlags = listOf(
                "Clients and internal business workstations connecting to the exact same WiFi SSID",
                "Former staff members retaining access to company email or cloud storage after departure",
                "Backups stored only on a single USB drive plugged permanently into the office PC",
                "Staff using personal unmanaged devices to access confidential client databases without screen locks"
            ),
            safeHabits = listOf(
                "Establish an immediate offboarding checklist: revoke email, cloud drives, and remote access the day an employee leaves",
                "Implement the 3-2-1 backup rule: 3 copies of data, on 2 different media types, with 1 copy stored securely offsite or in the cloud",
                "Set up a dedicated 'Guest' WiFi network isolated from company computers and point-of-sale systems",
                "Conduct quarterly cybersecurity awareness refreshers for all team members"
            ),
            keyTakeaways = "Cybersecurity for SMEs is not about expensive enterprise tools; it begins with basic hygiene: backups, access control, and staff awareness.",
            position = 1
        ),
        LessonEntity(
            id = "lesson-10-1",
            courseId = "course-10",
            title = "Understanding POPIA & Your Personal Data Rights",
            summary = "What the Protection of Personal Information Act requires and how to respond to data leaks.",
            scenario = "Lindiwe signs up for a loyalty program at a local retail store. Three weeks later, she starts receiving dozens of unsolicited marketing calls for insurance policies. She suspects her personal details were sold or improperly shared without her consent.",
            howItWorks = "Under POPIA (Protection of Personal Information Act), South African businesses (responsible parties) may only process personal info with lawful justification or clear consent. Selling or leaking data without authorization violates the Act.",
            redFlags = listOf(
                "Businesses requiring unnecessary sensitive data (e.g. copy of ID for simple newsletter signup)",
                "Pre-ticked consent boxes authorizing sharing data with third-party partners",
                "No visible Privacy Policy or contact information for an Information Officer",
                "Failure of a company to notify affected customers following a confirmed data breach"
            ),
            safeHabits = listOf(
                "You have the legal right to ask any South African business what personal data they hold on you and request its deletion",
                "Opt out of direct marketing by demanding to be placed on their internal 'Do Not Contact' list",
                "Report continuous unlawful direct marketing or data breaches to the Information Regulator of South Africa (inforegulator.org.za)",
                "Be cautious of physical forms that ask for your ID number when entering office parks or shops"
            ),
            keyTakeaways = "POPIA empowers you with rights to privacy, consent, and notification. Companies must safeguard your data or face legal penalties.",
            position = 1
        )
    )

    val quizQuestions = listOf(
        // Course 1 Questions
        QuizQuestionEntity(
            id = "q-1-1",
            courseId = "course-1",
            question = "You receive an email claiming to be from SARS with a link to claim an overdue refund. The sender is 'returns@sars-tax-refund.org'. What should you do?",
            options = listOf(
                "Click the link immediately to prevent the refund expiring",
                "Reply to the email asking for confirmation of your tax number",
                "Delete or forward to phishing@sars.gov.za; never click refund links in emails",
                "Enter your banking details so they can verify your account"
            ),
            correctAnswerIndex = 2,
            explanation = "SARS never sends direct links to claim refunds via email. Legitimate SARS correspondence comes from '@sars.gov.za', and you should only access your profile by typing www.sarsefiling.co.za directly.",
            questionType = "multiple_choice",
            position = 1
        ),
        QuizQuestionEntity(
            id = "q-1-2",
            courseId = "course-1",
            question = "True or False: Legitimate banks will routinely ask for your Internet banking password or card CVV via email to 'upgrade security'.",
            options = listOf("True", "False"),
            correctAnswerIndex = 1,
            explanation = "False! No reputable South African financial institution will ever ask for your password, PIN, or CVV code via email, SMS, or telephone.",
            questionType = "true_false",
            position = 2
        ),
        QuizQuestionEntity(
            id = "q-1-3",
            courseId = "course-1",
            question = "A regular supplier emails you stating their bank account has changed and requests EFT to a new bank. What is the safest next step?",
            options = listOf(
                "Pay the new account immediately to avoid late payment penalties",
                "Call the supplier on their trusted, known phone number to verify the change verbally",
                "Reply to the email asking 'Is this really you?'",
                "Forward the invoice to another supplier for advice"
            ),
            correctAnswerIndex = 1,
            explanation = "Always verify banking detail changes out-of-band using a verified phone number you already have on file, never the phone number printed on the new invoice.",
            questionType = "multiple_choice",
            position = 3
        ),

        // Course 2 Questions
        QuizQuestionEntity(
            id = "q-2-1",
            courseId = "course-2",
            question = "You receive a WhatsApp message from an unknown number: 'Hi Dad, I broke my phone, this is my new number. Can you send R2,500 urgently?'. What is the correct response?",
            options = listOf(
                "EFT the money right away because it sounds urgent",
                "Call your child's original phone number or ask a private family security question",
                "Send your credit card details over WhatsApp",
                "Ask the scammer to send their location"
            ),
            correctAnswerIndex = 1,
            explanation = "This is the classic 'Hi Mom/Dad' impersonation scam. Always stop and call the family member's known phone number or test them with a question only they would know.",
            questionType = "multiple_choice",
            position = 1
        ),
        QuizQuestionEntity(
            id = "q-2-2",
            courseId = "course-2",
            question = "An SMS arrives: 'SAPost: Parcel held due to R25 fee. Pay at http://sapo-clearance-za.cc'. What indicates this is fraudulent?",
            options = listOf(
                "The domain name 'sapo-clearance-za.cc' is not the official South African Post Office domain",
                "It was sent from an ordinary mobile number rather than a legitimate company shortcode",
                "Official couriers do not demand credit card payments on strange web links for address issues",
                "All of the above"
            ),
            correctAnswerIndex = 3,
            explanation = "All of these are major red flags! The unofficial domain, mobile sender number, and suspicious fee request indicate an active smishing campaign.",
            questionType = "multiple_choice",
            position = 2
        ),
        QuizQuestionEntity(
            id = "q-2-3",
            courseId = "course-2",
            question = "True or False: If an SMS OTP message says 'Do not share this code with anyone, including bank staff', it is safe to give it to a bank caller investigating fraud on your account.",
            options = listOf("True", "False"),
            correctAnswerIndex = 1,
            explanation = "False! Legitimate bank staff will never ask for an OTP. Anyone asking for your OTP is actively trying to authorize a transaction or take over your account.",
            questionType = "true_false",
            position = 3
        ),

        // Course 3 Questions
        QuizQuestionEntity(
            id = "q-3-1",
            courseId = "course-3",
            question = "You sell an item on Facebook Marketplace. The buyer sends an SMS and a PDF Proof of Payment. When should you hand over the item?",
            options = listOf(
                "As soon as you see the PDF looks official with a bank stamp",
                "When the driver arrives at your gate to save them waiting",
                "Only after you log into your banking app and confirm the money is in your AVAILABLE balance",
                "Immediately if the buyer seems polite and well-spoken"
            ),
            correctAnswerIndex = 2,
            explanation = "Proof of Payment PDFs and SMS notifications are easily forged or can bounce (e.g. fraudulent cheques). Never release goods until funds are cleared in your available balance.",
            questionType = "multiple_choice",
            position = 1
        ),
        QuizQuestionEntity(
            id = "q-3-2",
            courseId = "course-3",
            question = "True or False: A cheque deposit that appears in your bank balance as 'Uncleared' or 'Ledger Balance' can still be reversed or dishonored days later.",
            options = listOf("True", "False"),
            correctAnswerIndex = 0,
            explanation = "True! Fraudsters often deposit stolen or empty cheques. The ledger balance reflects the deposit attempt, but funds reverse once the cheque bounces.",
            questionType = "true_false",
            position = 2
        ),

        // Course 4 Questions
        QuizQuestionEntity(
            id = "q-4-1",
            courseId = "course-4",
            question = "Which of the following is considered the STRONGEST password strategy?",
            options = listOf(
                "Short 8-character password with numbers like 'Pa55w0rd!'",
                "A 4-word random passphrase like 'bakkie-protea-solar-sunset'",
                "Your pet's name followed by your birth year",
                "Reusing one very complex password on every website"
            ),
            correctAnswerIndex = 1,
            explanation = "Passphrases of 4+ random words combine massive cryptographic entropy with ease of human recall, rendering brute-force attacks computationally impractical.",
            questionType = "multiple_choice",
            position = 1
        ),
        QuizQuestionEntity(
            id = "q-4-2",
            courseId = "course-4",
            question = "Why is reusing the same password across multiple online accounts dangerous?",
            options = listOf(
                "It uses too much phone memory",
                "If any single low-security site gets breached, attackers use credential stuffing to unlock your important accounts",
                "Password managers will crash",
                "Your ISP will block your connection"
            ),
            correctAnswerIndex = 1,
            explanation = "In credential stuffing, hackers test breached username/password pairs automatically across banks, email, and shopping sites.",
            questionType = "multiple_choice",
            position = 2
        ),

        // Course 5 Questions
        QuizQuestionEntity(
            id = "q-5-1",
            courseId = "course-5",
            question = "Why is an authenticator app (e.g. Google Authenticator) safer than SMS for Multi-Factor Authentication?",
            options = listOf(
                "Authenticator apps do not need any battery",
                "SMS OTPs can be intercepted via SIM-swap attacks or telecom vulnerabilities, whereas app codes stay on the device",
                "SMS OTPs only work during business hours",
                "Authenticator apps automatically pay your phone bill"
            ),
            correctAnswerIndex = 1,
            explanation = "In a SIM-swap fraud, criminals hijack your mobile number and receive your SMS OTPs. Authenticator apps generate codes locally on your hardware.",
            questionType = "multiple_choice",
            position = 1
        ),
        QuizQuestionEntity(
            id = "q-5-2",
            courseId = "course-5",
            question = "Your phone suddenly loses cellular signal and says 'Emergency Calls Only' in an area where you normally have full LTE. What threat should you suspect?",
            options = listOf(
                "An unauthorized SIM swap may be underway on your number",
                "Your phone camera is broken",
                "The weather is cloudy",
                "Your email password expired"
            ),
            correctAnswerIndex = 0,
            explanation = "A sudden unexplained loss of network connectivity can indicate a SIM swap. Call your cellular provider immediately from another phone.",
            questionType = "multiple_choice",
            position = 2
        ),

        // Course 6 Questions
        QuizQuestionEntity(
            id = "q-6-1",
            courseId = "course-6",
            question = "A caller claiming to be from your bank's 'Fraud Prevention Unit' says your account is compromised and asks you to transfer your money to a 'safe holding account'. What is happening?",
            options = listOf(
                "The bank is protecting you; transfer the money immediately",
                "This is an authorized bank procedure",
                "This is a social engineering scam; banks NEVER ask customers to transfer funds into 'safe accounts'",
                "You should ask the caller for their staff number then comply"
            ),
            correctAnswerIndex = 2,
            explanation = "Banks never ask customers to transfer money into 'safe accounts' or 'holding vaults'. This is a psychological manipulation tactic designed to drain your savings.",
            questionType = "multiple_choice",
            position = 1
        ),
        QuizQuestionEntity(
            id = "q-6-2",
            courseId = "course-6",
            question = "True or False: Cybercriminals often create a false sense of extreme urgency to prevent victims from thinking critically.",
            options = listOf("True", "False"),
            correctAnswerIndex = 0,
            explanation = "True! Urgency ('Act in 10 minutes or your account will be blocked!') bypasses logical scrutiny and forces emotional compliance.",
            questionType = "true_false",
            position = 2
        ),

        // Course 7 Questions
        QuizQuestionEntity(
            id = "q-7-1",
            courseId = "course-7",
            question = "You click a link to a banking site. The URL says 'https://www.standardbank-secure-login.co'. Is this safe?",
            options = listOf(
                "Yes, because it begins with 'https://'",
                "No! The legitimate domain is 'standardbank.co.za'. Scammers register lookalike domains with deceptive words",
                "Yes, 'secure-login' confirms it is verified",
                "Yes, all .co websites are banks"
            ),
            correctAnswerIndex = 1,
            explanation = "Having 'https' only means the connection is encrypted, not that the owner is legitimate. Scammers frequently register deceptive lookalike domains.",
            questionType = "multiple_choice",
            position = 1
        ),

        // Course 8 Questions
        QuizQuestionEntity(
            id = "q-8-1",
            courseId = "course-8",
            question = "What is the best habit when disposing of physical paper bank statements or utility bills in South Africa?",
            options = listOf(
                "Throw them directly into the municipal wheelie bin",
                "Shred or completely destroy them to prevent identity theft via dumpster diving",
                "Fold them in half before throwing away",
                "Leave them outside for recycling collectors"
            ),
            correctAnswerIndex = 1,
            explanation = "Dumpster diving is a common method criminals use to find your ID number, full names, and proof of residence to open fraudulent store accounts in your name.",
            questionType = "multiple_choice",
            position = 1
        ),

        // Course 9 Questions
        QuizQuestionEntity(
            id = "q-9-1",
            courseId = "course-9",
            question = "What does the '3-2-1 Backup Rule' stand for in small business disaster recovery?",
            options = listOf(
                "3 backups on 2 different devices with 1 stored securely offsite or in the cloud",
                "Backing up every 3 weeks for 2 hours on 1 computer",
                "3 passwords, 2 users, 1 email",
                "Restarting your computer 3 times a day"
            ),
            correctAnswerIndex = 0,
            explanation = "The 3-2-1 rule ensures that even if local hardware fails, gets stolen, or is encrypted by ransomware, a safe offsite copy exists to restore your business operations.",
            questionType = "multiple_choice",
            position = 1
        ),
        QuizQuestionEntity(
            id = "q-9-2",
            courseId = "course-9",
            question = "When an employee leaves a company, when should their access to business email and cloud drives be revoked?",
            options = listOf(
                "At the end of the month during accounting review",
                "Only if they ask for their email to be deleted",
                "Immediately on or before their final working day",
                "After six months of inactivity"
            ),
            correctAnswerIndex = 2,
            explanation = "Prompt offboarding prevents disgruntled ex-employees or lost employee devices from creating security breaches or leaking confidential business data.",
            questionType = "multiple_choice",
            position = 2
        ),

        // Course 10 Questions
        QuizQuestionEntity(
            id = "q-10-1",
            courseId = "course-10",
            question = "Under South Africa's POPIA legislation, what must a business do if there are reasonable grounds to believe customer personal data was breached?",
            options = listOf(
                "Keep it secret to protect company reputation",
                "Notify both the Information Regulator and the affected data subjects as soon as reasonably possible",
                "Delete all company records so no evidence remains",
                "Pay the hackers a ransom without telling anyone"
            ),
            correctAnswerIndex = 1,
            explanation = "Section 22 of POPIA legally obligates businesses to notify both the Information Regulator and the affected individuals when personal data has been compromised.",
            questionType = "multiple_choice",
            position = 1
        )
    )

    val checklistItems = listOf(
        // Account security
        ChecklistItemEntity(
            id = "chk-1",
            title = "Unique Passwords for Business Accounts",
            description = "Ensure business email, banking, accounting software, and admin panels use distinct, non-reused passphrases.",
            category = "Account Security",
            recommendedAction = "Deploy a business password manager (e.g. Bitwarden or 1Password) so team members do not reuse personal passwords.",
            position = 1
        ),
        ChecklistItemEntity(
            id = "chk-2",
            title = "Multi-Factor Authentication (MFA) Enabled",
            description = "Enforce MFA across all critical business accounts, prioritizing app-based authenticators over SMS.",
            category = "Account Security",
            recommendedAction = "Turn on MFA in Google Workspace, Microsoft 365, Xero, and banking portals.",
            position = 2
        ),
        ChecklistItemEntity(
            id = "chk-3",
            title = "Former Employees Access Revocation",
            description = "Revoke email, cloud drives, VPN, and internal system access immediately whenever an employee leaves.",
            category = "Account Security",
            recommendedAction = "Maintain a formal offboarding checklist signed by the direct manager on the employee's departure day.",
            position = 3
        ),
        ChecklistItemEntity(
            id = "chk-4",
            title = "Restricted Administrative Access",
            description = "Staff use standard non-admin accounts for daily work; administrator privileges are strictly reserved for IT maintenance.",
            category = "Account Security",
            recommendedAction = "Audit user permissions monthly to prevent privilege creep and remove unnecessary admin roles.",
            position = 4
        ),

        // Devices and software
        ChecklistItemEntity(
            id = "chk-5",
            title = "Operating Systems and Software Patched",
            description = "Enable automatic updates for Windows, macOS, Android, iOS, and critical business applications.",
            category = "Devices and Software",
            recommendedAction = "Schedule weekly maintenance windows or turn on automatic patching for OS and browser updates.",
            position = 5
        ),
        ChecklistItemEntity(
            id = "chk-6",
            title = "Device Screen Locks & Auto-Lock Timers",
            description = "All business laptops, tablets, and phones enforce PINs, biometric locks, or passcodes with a 3-5 minute auto-lock timeout.",
            category = "Devices and Software",
            recommendedAction = "Configure group policy or device management to mandate screen locks on all company-used devices.",
            position = 6
        ),
        ChecklistItemEntity(
            id = "chk-7",
            title = "Endpoint Security Software Maintained",
            description = "Workstations have reputable endpoint protection (antivirus/EDR) active with real-time definitions updating daily.",
            category = "Devices and Software",
            recommendedAction = "Check that Microsoft Defender or equivalent endpoint software is running and scans weekly.",
            position = 7
        ),
        ChecklistItemEntity(
            id = "chk-8",
            title = "Unnecessary & Shadow IT Applications Removed",
            description = "Prevent installation of unauthorized third-party software, peer-to-peer apps, or unvetted browser extensions.",
            category = "Devices and Software",
            recommendedAction = "Conduct a software inventory and uninstall legacy, unpatched, or unnecessary programs.",
            position = 8
        ),

        // Data protection
        ChecklistItemEntity(
            id = "chk-9",
            title = "3-2-1 Backup Strategy Implemented",
            description = "Maintain 3 copies of critical business data, on 2 different media formats, with 1 copy stored securely offsite or in cloud backup.",
            category = "Data Protection",
            recommendedAction = "Set up automated daily cloud backups for financial databases and key customer records.",
            position = 9
        ),
        ChecklistItemEntity(
            id = "chk-10",
            title = "Periodic Backup Recovery Testing",
            description = "Test restoring data from backups at least once every quarter to ensure backup files are not corrupt.",
            category = "Data Protection",
            recommendedAction = "Run a test restoration of a sample database or folder to confirm your team can recover within hours.",
            position = 10
        ),
        ChecklistItemEntity(
            id = "chk-11",
            title = "Sensitive Files Access Tiers & POPIA Compliance",
            description = "Confidential customer details and payroll records are encrypted and accessible only to designated personnel.",
            category = "Data Protection",
            recommendedAction = "Classify data into Public, Internal, and Confidential. Enforce strict folder permissions.",
            position = 11
        ),

        // Employee awareness
        ChecklistItemEntity(
            id = "chk-12",
            title = "Employee Phishing & Scam Training",
            description = "Conduct regular awareness sessions on South African cyber threats (SARS refund scams, WhatsApp impersonation, invoice fraud).",
            category = "Employee Awareness",
            recommendedAction = "Use CyberShield Academy courses during employee onboarding and run quarterly refresher discussions.",
            position = 12
        ),
        ChecklistItemEntity(
            id = "chk-13",
            title = "Clear Reporting Workflow for Suspicious Activity",
            description = "Staff know exactly how and to whom to report suspicious emails, unexpected bank detail changes, or phone calls.",
            category = "Employee Awareness",
            recommendedAction = "Set up a dedicated internal mailbox (e.g. security@yourcompany.co.za) and celebrate employees who report threats.",
            position = 13
        ),

        // Incident readiness
        ChecklistItemEntity(
            id = "chk-14",
            title = "Documented Incident Response Contact Roster",
            description = "Emergency contacts for your IT provider, bank fraud hotline, insurer, and legal counsel are documented offline.",
            category = "Incident Readiness",
            recommendedAction = "Print a physical laminated card with IT support, bank fraud numbers, and leadership contact details.",
            position = 14
        ),
        ChecklistItemEntity(
            id = "chk-15",
            title = "Data Breach Notification Procedure (Information Regulator)",
            description = "The business has a documented process for reporting security compromises to the Information Regulator under POPIA Section 22.",
            category = "Incident Readiness",
            recommendedAction = "Bookmark the Information Regulator breach portal (inforegulator.org.za) and draft an emergency notification template.",
            position = 15
        )
    )

    val threatAlerts = listOf(
        ThreatAlert(
            id = "alert-1",
            title = "SARS eFiling Tax Refund Phishing Wave",
            category = "Email Phishing",
            severity = "Critical",
            summary = "Attackers are sending mass emails claiming 'SARS has approved an unexpected refund of R7,450'. Links direct to fake login portals mimicking the eFiling homepage.",
            indicatorOfCompromise = "Deceptive sender domains like 'sars-efiling-portal-sa.online' and 'sars-gov-tax.top'.",
            officialAdvice = "Never click links in SARS correspondence. Access your profile exclusively by typing 'www.sarsefiling.co.za' directly into your browser.",
            date = "Verified: Oct 2026"
        ),
        ThreatAlert(
            id = "alert-2",
            title = "Capitec & FNB SMS Banking Notification Smishing",
            category = "SMS / Smishing",
            severity = "High",
            summary = "SMS messages pretending to be bank transaction confirmations ('A payment of R4,200 has been debited. If this was not you, click link to reverse').",
            indicatorOfCompromise = "Shortlinks leading to cloned banking login screens designed to harvest card numbers and ATM PINs.",
            officialAdvice = "Your bank will never send a link in an SMS to cancel a transaction. Check your bank app directly and phone the number on the back of your card.",
            date = "Verified: Oct 2026"
        ),
        ThreatAlert(
            id = "alert-3",
            title = "Fake WhatsApp Job Offer & Task Scam",
            category = "Social Engineering",
            severity = "Warning",
            summary = "Scammers contact South Africans on WhatsApp offering R1,500/day for liking YouTube videos or rating Google Maps locations, then demand 'deposit fees'.",
            indicatorOfCompromise = "Unsolicited messages from foreign or virtual numbers (+27 60, +44, +234) offering unrealistic daily pay for trivial tasks.",
            officialAdvice = "Legitimate companies never ask job applicants to pay registration fees or deposit money into cryptocurrency wallets.",
            date = "Verified: Oct 2026"
        )
    )

    val dailyTips = listOf(
        DailyTip(
            id = "tip-1",
            title = "The 5-Second Sender Inspection",
            tip = "Before clicking any link in an email, hover over or tap the sender name to reveal the full email address behind it.",
            category = "Email Defense",
            practicalStep = "Look at the letters after the '@' sign. If it says '@sars-notice.com' instead of '@sars.gov.za', it's a scam."
        ),
        DailyTip(
            id = "tip-2",
            title = "Passphrases Beat Complex Passwords",
            tip = "Four random dictionary words (e.g., 'table-mountain-sunny-breeze') are 10,000x harder to crack than 'P@ssword1'.",
            category = "Password Security",
            practicalStep = "Choose four random words you can picture in your mind. No need to memorize confusing symbols."
        ),
        DailyTip(
            id = "tip-3",
            title = "Never Share Your OTP",
            tip = "Even if a caller sounds professional, polite, and claims to be from your bank's fraud team, never read out an OTP.",
            category = "Banking Safety",
            practicalStep = "Banks have systems to block cards without needing your OTP. An OTP is only used to AUTHORIZE spending."
        ),
        DailyTip(
            id = "tip-4",
            title = "Inspect Bank Account Details Out-of-Band",
            tip = "If a contractor or vendor emails you saying their bank details have changed, pick up the phone and call their office.",
            category = "Business Protection",
            practicalStep = "Use the telephone number from your original paper contract, never the phone number inside the new email."
        ),
        DailyTip(
            id = "tip-5",
            title = "Lock Down Your Social Media Privacy",
            tip = "Scammers use details like your hometown, pet names, and family photos to impersonate you to your parents on WhatsApp.",
            category = "Privacy & POPIA",
            practicalStep = "Set your Facebook and Instagram profiles to 'Friends Only' and hide your mobile number from public search."
        )
    )
}
