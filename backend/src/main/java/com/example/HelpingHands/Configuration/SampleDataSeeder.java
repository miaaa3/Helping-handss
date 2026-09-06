package com.example.HelpingHands.Configuration;

import com.example.HelpingHands.Entity.ApplicationStatus;
import com.example.HelpingHands.Entity.Comment;
import com.example.HelpingHands.Entity.Donation;
import com.example.HelpingHands.Entity.DonationStatus;
import com.example.HelpingHands.Entity.Follow;
import com.example.HelpingHands.Entity.Gender;
import com.example.HelpingHands.Entity.Like;
import com.example.HelpingHands.Entity.Message;
import com.example.HelpingHands.Entity.Notification;
import com.example.HelpingHands.Entity.Opportunity;
import com.example.HelpingHands.Entity.OpportunityApplication;
import com.example.HelpingHands.Entity.OpportunityCategory;
import com.example.HelpingHands.Entity.OpportunityStatus;
import com.example.HelpingHands.Entity.Organization;
import com.example.HelpingHands.Entity.OrganizationVerificationStatus;
import com.example.HelpingHands.Entity.Post;
import com.example.HelpingHands.Entity.UserEntity;
import com.example.HelpingHands.Entity.Volunteer;
import com.example.HelpingHands.Repository.CommentRepository;
import com.example.HelpingHands.Repository.DonationRepository;
import com.example.HelpingHands.Repository.FollowRepository;
import com.example.HelpingHands.Repository.LikeRepository;
import com.example.HelpingHands.Repository.MessageRepository;
import com.example.HelpingHands.Repository.NotificationRepository;
import com.example.HelpingHands.Repository.OpportunityApplicationRepository;
import com.example.HelpingHands.Repository.OpportunityRepository;
import com.example.HelpingHands.Repository.OrganizationRepository;
import com.example.HelpingHands.Repository.PostRepository;
import com.example.HelpingHands.Repository.UserRepository;
import com.example.HelpingHands.Repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class SampleDataSeeder {

    private static final String SAMPLE_PASSWORD = "Password123!";

    private final UserRepository userRepository;
    private final VolunteerRepository volunteerRepository;
    private final OrganizationRepository organizationRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final MessageRepository messageRepository;
    private final DonationRepository donationRepository;
    private final OpportunityRepository opportunityRepository;
    private final OpportunityApplicationRepository applicationRepository;
    private final NotificationRepository notificationRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @ConditionalOnProperty(name = "sample-data.enabled", havingValue = "true")
    CommandLineRunner seedSampleData() {
        return args -> {
            UserEntity admin = ensureAdmin();

            Volunteer maya = ensureVolunteer("maya.volunteer@helpinghands.test", "maya", "Maya Carter",
                    "Paris, France", "+33 6 10 20 30 40", Gender.FEMALE,
                    "Student volunteer focused on food security and mentoring.", Set.of("Food distribution", "Education", "Community events"));
            Volunteer theo = ensureVolunteer("theo.volunteer@helpinghands.test", "theo", "Theo Martin",
                    "Lyon, France", "+33 6 11 22 33 44", Gender.MALE,
                    "Logistics helper who loves environmental action days.", Set.of("Environment", "Animal care", "Logistics"));
            Volunteer lina = ensureVolunteer("lina.volunteer@helpinghands.test", "lina", "Lina Haddad",
                    "Marseille, France", "+33 6 55 44 33 22", Gender.FEMALE,
                    "Healthcare student available for tutoring and community health events.", Set.of("Health", "Tutoring", "Translation"));
            Volunteer sofia = ensureVolunteer("sofia.volunteer@helpinghands.test", "sofia", "Sofia Nguyen",
                    "Paris, France", "+33 6 70 80 90 10", Gender.FEMALE,
                    "Designer and event host helping local associations communicate clearly.", Set.of("Design", "Events", "Animal care"));
            Volunteer adam = ensureVolunteer("adam.volunteer@helpinghands.test", "adam", "Adam Williams",
                    "Nice, France", "+33 6 12 98 76 54", Gender.MALE,
                    "Weekend driver and fundraiser for neighborhood support projects.", Set.of("Driving", "Fundraising", "Disaster relief"));
            Volunteer ines = ensureVolunteer("ines.volunteer@helpinghands.test", "ines", "Ines Moreau",
                    "Toulouse, France", "+33 6 44 22 88 11", Gender.FEMALE,
                    "Retired teacher supporting literacy, homework help, and senior visits.", Set.of("Education", "Seniors", "Community"));
            Volunteer noah = ensureVolunteer("noah.volunteer@helpinghands.test", "noah", "Noah Garcia",
                    "Bordeaux, France", "+33 6 83 18 20 21", Gender.MALE,
                    "First-aid certified volunteer for health events and emergency response.", Set.of("Health", "First aid", "Disaster relief"));
            Volunteer camille = ensureVolunteer("camille.volunteer@helpinghands.test", "camille", "Camille Robert",
                    "Lille, France", "+33 6 32 45 66 77", Gender.OTHER,
                    "Community organizer interested in accessibility and inclusive events.", Set.of("Accessibility", "Community events", "Translation"));

            Organization foodBridge = ensureOrganization("foodbridge.org@helpinghands.test", "foodbridge", "FoodBridge Paris",
                    "Paris, France", "+33 1 44 20 10 10",
                    "FoodBridge rescues surplus meals and distributes them to families, students, and seniors.",
                    "Food aid", "Amelie Laurent", OrganizationVerificationStatus.VERIFIED);
            Organization greenSteps = ensureOrganization("greensteps.org@helpinghands.test", "greensteps", "GreenSteps",
                    "Lyon, France", "+33 4 72 00 10 10",
                    "GreenSteps organizes neighborhood cleanups, urban garden days, and recycling workshops.",
                    "Environment", "Noah Bernard", OrganizationVerificationStatus.VERIFIED);
            Organization brightFuture = ensureOrganization("brightfuture.org@helpinghands.test", "brightfuture", "Bright Future Tutors",
                    "Remote / Marseille", "+33 4 91 00 20 20",
                    "Bright Future Tutors connects volunteer mentors with students who need free homework support.",
                    "Education", "Sarah Benali", OrganizationVerificationStatus.PENDING);
            Organization pawsCare = ensureOrganization("pawscare.org@helpinghands.test", "pawscare", "PawsCare Shelter",
                    "Nice, France", "+33 4 93 00 40 40",
                    "PawsCare supports animal shelters with adoption days, transport, and foster coordination.",
                    "Animal welfare", "Claire Dubois", OrganizationVerificationStatus.VERIFIED);
            Organization reliefNow = ensureOrganization("reliefnow.org@helpinghands.test", "reliefnow", "ReliefNow",
                    "Toulouse, France", "+33 5 61 00 33 33",
                    "ReliefNow prepares local emergency kits and supports families after floods and fires.",
                    "Disaster relief", "Marc Petit", OrganizationVerificationStatus.PENDING);
            Organization seniorCircle = ensureOrganization("seniorcircle.org@helpinghands.test", "seniorcircle", "Senior Circle",
                    "Lille, France", "+33 3 20 00 50 50",
                    "Senior Circle organizes friendly visits, phone check-ins, and digital help for older adults.",
                    "Senior support", "Elise Fournier", OrganizationVerificationStatus.REJECTED);

            List<UserEntity> allUsers = List.of(admin, maya, theo, lina, sofia, adam, ines, noah, camille,
                    foodBridge, greenSteps, brightFuture, pawsCare, reliefNow, seniorCircle);
            ensureFollows(allUsers, List.of(maya, theo, lina, sofia, adam, ines, noah, camille),
                    List.of(foodBridge, greenSteps, brightFuture, pawsCare, reliefNow, seniorCircle));

            Post foodPost = ensurePost(foodBridge, "We packed 120 grocery kits tonight. Huge thanks to everyone who helped after work.");
            Post greenPost = ensurePost(greenSteps, "Saturday cleanup route is ready. Bring gloves if you have them, we will provide bags and water.");
            Post tutorPost = ensurePost(brightFuture, "First tutoring session went great. Two students asked for extra math practice next week.");
            Post shelterPost = ensurePost(pawsCare, "Adoption day needs calm volunteers for check-in, dog walking, and photo support.");
            Post reliefPost = ensurePost(reliefNow, "We are building emergency kits this month and need help checking expiry dates.");
            Post seniorPost = ensurePost(seniorCircle, "Our digital-help afternoon paired 18 seniors with patient volunteers. Thank you.");
            Post mayaPost = ensurePost(maya, "I am free this weekend for food distribution or event setup near Paris.");
            Post theoPost = ensurePost(theo, "Looking for more environmental volunteering days around Lyon. Happy to help with logistics.");
            Post linaPost = ensurePost(lina, "Remote tutoring is perfect for my schedule this semester.");
            Post sofiaPost = ensurePost(sofia, "I can help design simple posters for verified organizations planning community events.");

            ensureComment(maya, foodPost, "Count me in for the next packing night.");
            ensureComment(theo, greenPost, "I can bring two friends to the cleanup.");
            ensureComment(lina, tutorPost, "Remote tutoring is perfect for my schedule.");
            ensureComment(foodBridge, mayaPost, "Thank you, Maya. We will message you the details.");
            ensureComment(sofia, shelterPost, "I can help with photos and check-in.");
            ensureComment(noah, reliefPost, "I have first-aid kit inventory experience.");
            ensureComment(ines, seniorPost, "Happy to help with digital basics next week.");
            ensureComment(greenSteps, theoPost, "We have a riverbank cleanup open now.");

            ensureLikes(List.of(maya, theo, lina, sofia, adam, ines, noah, camille, foodBridge, greenSteps, brightFuture),
                    List.of(foodPost, greenPost, tutorPost, shelterPost, reliefPost, seniorPost, mayaPost, theoPost, linaPost, sofiaPost));

            Opportunity pantry = ensureOpportunity(foodBridge, "Evening Pantry Packing",
                    "Sort donated groceries and prepare family food kits for the weekend distribution.",
                    OpportunityCategory.COMMUNITY, LocalDateTime.now().plusDays(4).withHour(18).withMinute(30), "Paris 11e Community Center", 12, OpportunityStatus.OPEN);
            Opportunity cleanup = ensureOpportunity(greenSteps, "Riverbank Cleanup Morning",
                    "Help remove litter, separate recyclables, and map areas that need city pickup support.",
                    OpportunityCategory.ENVIRONMENT, LocalDateTime.now().plusDays(9).withHour(9).withMinute(0), "Lyon, Parc de la Tete d'Or", 20, OpportunityStatus.OPEN);
            Opportunity tutoring = ensureOpportunity(brightFuture, "Remote Homework Mentoring",
                    "Support middle-school students with math, English, and study planning over video call.",
                    OpportunityCategory.EDUCATION, LocalDateTime.now().plusDays(2).withHour(17).withMinute(0), "Remote", 8, OpportunityStatus.OPEN);
            Opportunity firstAid = ensureOpportunity(reliefNow, "Community First Aid Workshop Support",
                    "Welcome attendees, prepare materials, and help instructors during a public health workshop.",
                    OpportunityCategory.HEALTH, LocalDateTime.now().plusDays(14).withHour(10).withMinute(0), "Toulouse Community Hall", 6, OpportunityStatus.OPEN);
            Opportunity adoption = ensureOpportunity(pawsCare, "Shelter Adoption Day",
                    "Guide visitors, walk calm dogs, refresh water bowls, and support adoption paperwork.",
                    OpportunityCategory.ANIMALS, LocalDateTime.now().plusDays(6).withHour(10).withMinute(0), "Nice Animal Shelter", 10, OpportunityStatus.OPEN);
            Opportunity seniorVisits = ensureOpportunity(seniorCircle, "Friendly Senior Visits",
                    "Visit isolated seniors for conversation, board games, and help reading letters.",
                    OpportunityCategory.COMMUNITY, LocalDateTime.now().plusDays(11).withHour(15).withMinute(0), "Lille", 14, OpportunityStatus.OPEN);
            Opportunity garden = ensureOpportunity(greenSteps, "Urban Garden Planting Day",
                    "Prepare soil, plant herbs, and set up labels for a shared neighborhood garden.",
                    OpportunityCategory.ENVIRONMENT, LocalDateTime.now().plusDays(18).withHour(9).withMinute(30), "Lyon Croix-Rousse", 16, OpportunityStatus.OPEN);
            Opportunity closedTutor = ensureOpportunity(brightFuture, "Winter Exam Prep Sprint",
                    "Completed mentoring sprint for students preparing winter exams.",
                    OpportunityCategory.EDUCATION, LocalDateTime.now().minusDays(12).withHour(16).withMinute(0), "Remote", 5, OpportunityStatus.CLOSED);
            Opportunity fullKitchen = ensureOpportunity(foodBridge, "Soup Kitchen Lunch Shift",
                    "Lunch service is fully staffed, but volunteers can follow FoodBridge for future shifts.",
                    OpportunityCategory.COMMUNITY, LocalDateTime.now().plusDays(3).withHour(11).withMinute(0), "Paris 19e", 5, OpportunityStatus.FULL);
            ensureOpportunity(reliefNow, "Draft Flood Preparedness Drill",
                    "Draft opportunity used to show organization-only draft visibility in dashboards.",
                    OpportunityCategory.DISASTER_RELIEF, LocalDateTime.now().plusDays(24).withHour(13).withMinute(0), "Toulouse", 12, OpportunityStatus.DRAFT);

            ensureApplication(pantry, maya, ApplicationStatus.ACCEPTED);
            ensureApplication(pantry, theo, ApplicationStatus.PENDING);
            ensureApplication(pantry, sofia, ApplicationStatus.PENDING);
            ensureApplication(cleanup, theo, ApplicationStatus.ACCEPTED);
            ensureApplication(cleanup, camille, ApplicationStatus.PENDING);
            ensureApplication(tutoring, lina, ApplicationStatus.ACCEPTED);
            ensureApplication(tutoring, maya, ApplicationStatus.REJECTED);
            ensureApplication(firstAid, noah, ApplicationStatus.PENDING);
            ensureApplication(firstAid, lina, ApplicationStatus.PENDING);
            ensureApplication(adoption, sofia, ApplicationStatus.ACCEPTED);
            ensureApplication(adoption, adam, ApplicationStatus.CANCELLED);
            ensureApplication(seniorVisits, ines, ApplicationStatus.ACCEPTED);
            ensureApplication(seniorVisits, camille, ApplicationStatus.PENDING);
            ensureApplication(garden, theo, ApplicationStatus.PENDING);
            ensureApplication(garden, maya, ApplicationStatus.CANCELLED);
            ensureApplication(closedTutor, ines, ApplicationStatus.ACCEPTED);
            ensureApplication(closedTutor, lina, ApplicationStatus.ACCEPTED);
            ensureApplication(fullKitchen, adam, ApplicationStatus.ACCEPTED);

            ensureMessages(maya, theo, lina, sofia, adam, foodBridge, greenSteps, brightFuture, pawsCare, reliefNow);
            ensureDonations(maya, theo, lina, sofia, adam, ines, noah, foodBridge, greenSteps, brightFuture, pawsCare, reliefNow, seniorCircle);
            ensureNotifications(maya, theo, lina, sofia, noah, foodBridge, greenSteps, brightFuture, pawsCare, reliefNow);
        };
    }

    private UserEntity ensureAdmin() {
        return userRepository.findByEmail("admin@helpinghands.test").orElseGet(() -> {
            UserEntity admin = new UserEntity("admin@helpinghands.test", passwordEncoder.encode(SAMPLE_PASSWORD),
                    "Paris, France", "+33 1 00 00 00 00", "ADMIN", "admin");
            admin.setBio("Platform administrator for organization verification, moderation, and user safety.");
            admin.setProfile("https://ui-avatars.com/api/?name=Admin&background=374151&color=fff");
            return userRepository.save(admin);
        });
    }

    private Volunteer ensureVolunteer(String email, String name, String fullName, String address, String phone,
                                      Gender gender, String bio, Set<String> interests) {
        return volunteerRepository.findByEmail(email).orElseGet(() -> {
            Volunteer created = new Volunteer(email, passwordEncoder.encode(SAMPLE_PASSWORD), address, phone,
                    "VOLUNTEER", name, fullName, gender, new Date());
            created.setProfile("https://ui-avatars.com/api/?name=" + fullName.replace(" ", "+") + "&background=2f855a&color=fff");
            created.setBio(bio);
            created.setInterests(interests);
            return volunteerRepository.save(created);
        });
    }

    private Organization ensureOrganization(String email, String name, String displayName, String address, String phone,
                                            String description, String type, String founder, OrganizationVerificationStatus status) {
        return organizationRepository.findByEmail(email).orElseGet(() -> {
            Organization created = new Organization(email, passwordEncoder.encode(SAMPLE_PASSWORD), address, phone,
                    "ORGANIZATION", name, description, type, founder, new Date());
            created.setProfile("https://ui-avatars.com/api/?name=" + displayName.replace(" ", "+") + "&background=2563eb&color=fff");
            created.setBio(description);
            created.setWebsite("https://" + name + ".example");
            created.setVerificationStatus(status);
            return organizationRepository.save(created);
        });
    }

    private void ensureFollows(List<UserEntity> allUsers, List<Volunteer> volunteers, List<Organization> organizations) {
        for (UserEntity user : allUsers) {
            ensureFollow(user, user);
        }
        for (Volunteer volunteer : volunteers) {
            for (Organization organization : organizations) {
                ensureFollow(volunteer, organization);
            }
        }
        ensureFollow(organizations.get(0), volunteers.get(0));
        ensureFollow(organizations.get(0), volunteers.get(3));
        ensureFollow(organizations.get(1), volunteers.get(1));
        ensureFollow(organizations.get(1), volunteers.get(7));
        ensureFollow(organizations.get(2), volunteers.get(2));
        ensureFollow(organizations.get(3), volunteers.get(3));
        ensureFollow(organizations.get(4), volunteers.get(6));
        ensureFollow(organizations.get(5), volunteers.get(5));
    }

    private void ensureFollow(UserEntity follower, UserEntity following) {
        if (!followRepository.existsByFollowerAndFollowing(follower, following)) {
            followRepository.save(new Follow(follower, following));
        }
    }

    private Post ensurePost(UserEntity user, String content) {
        return postRepository.findAll().stream()
                .filter(post -> content.equals(post.getContent()))
                .findFirst()
                .orElseGet(() -> postRepository.save(new Post(content, user)));
    }

    private void ensureComment(UserEntity user, Post post, String content) {
        boolean exists = commentRepository.findAll().stream()
                .anyMatch(comment -> sameId(post, comment.getPost()) && sameId(user, comment.getUser()) && content.equals(comment.getContent()));
        if (!exists) {
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setUser(user);
            comment.setPost(post);
            commentRepository.save(comment);
        }
    }

    private void ensureLikes(List<UserEntity> users, List<Post> posts) {
        for (Post post : posts) {
            for (int i = 0; i < users.size(); i++) {
                if ((post.getId() + i) % 3 != 0) {
                    ensureLike(users.get(i), post);
                }
            }
        }
    }

    private void ensureLike(UserEntity user, Post post) {
        boolean exists = likeRepository.findAll().stream()
                .anyMatch(like -> sameId(post, like.getPost()) && sameId(user, like.getUser()));
        if (!exists && !sameId(post.getUser(), user)) {
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            like.setIsLiked(true);
            likeRepository.save(like);
        }
    }

    private Opportunity ensureOpportunity(Organization organization, String title, String description,
                                          OpportunityCategory category, LocalDateTime date, String location,
                                          int neededVolunteers, OpportunityStatus status) {
        Opportunity opportunity = opportunityRepository.findAll().stream()
                .filter(existing -> title.equals(existing.getTitle()))
                .findFirst()
                .orElseGet(Opportunity::new);
        opportunity.setTitle(title);
        opportunity.setDescription(description);
        opportunity.setCategory(category);
        opportunity.setDate(date);
        opportunity.setLocation(location);
        opportunity.setNeededVolunteers(neededVolunteers);
        opportunity.setStatus(status);
        opportunity.setOrganization(organization);
        return opportunityRepository.save(opportunity);
    }

    private void ensureApplication(Opportunity opportunity, Volunteer volunteer, ApplicationStatus status) {
        OpportunityApplication application = applicationRepository.findByOpportunityIdOrderByCreatedAtDesc(opportunity.getId()).stream()
                .filter(existing -> sameId(existing.getVolunteer(), volunteer))
                .findFirst()
                .orElseGet(() -> new OpportunityApplication(opportunity, volunteer));
        application.setStatus(status);
        applicationRepository.save(application);
    }

    private void ensureMessages(Volunteer maya, Volunteer theo, Volunteer lina, Volunteer sofia, Volunteer adam,
                                Organization foodBridge, Organization greenSteps, Organization brightFuture,
                                Organization pawsCare, Organization reliefNow) {
        ensureMessage(maya, foodBridge, "Hi FoodBridge, I can help with packing this week if you still need volunteers.", true);
        ensureMessage(foodBridge, maya, "Yes please. Thursday at 18:30 would be perfect.", false);
        ensureMessage(theo, greenSteps, "Do you need someone to coordinate supplies for the cleanup?", true);
        ensureMessage(greenSteps, theo, "That would help a lot. We need bags, gloves, and two check-in volunteers.", false);
        ensureMessage(brightFuture, maya, "Thanks for applying to mentor. Are Tuesdays good for you?", false);
        ensureMessage(sofia, pawsCare, "I can photograph the adoption day and help welcome visitors.", true);
        ensureMessage(pawsCare, sofia, "That would be wonderful. We will send the volunteer briefing.", false);
        ensureMessage(adam, reliefNow, "I have a car and can deliver emergency kits around Nice next weekend.", true);
        ensureMessage(reliefNow, adam, "Thanks Adam. We may need drivers after the kit assembly.", false);
        ensureMessage(lina, brightFuture, "Can I mentor two students if the sessions are on different days?", true);
    }

    private void ensureMessage(UserEntity sender, UserEntity receiver, String content, boolean isRead) {
        boolean exists = messageRepository.findAll().stream().anyMatch(message -> content.equals(message.getContent())
                && sameId(sender, message.getSender()) && sameId(receiver, message.getReceiver()));
        if (!exists) {
            messageRepository.save(Message.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .content(content)
                    .isRead(isRead)
                    .build());
        }
    }

    private void ensureDonations(Volunteer maya, Volunteer theo, Volunteer lina, Volunteer sofia, Volunteer adam,
                                 Volunteer ines, Volunteer noah, Organization foodBridge, Organization greenSteps,
                                 Organization brightFuture, Organization pawsCare, Organization reliefNow,
                                 Organization seniorCircle) {
        ensureDonation(maya, foodBridge, "35.00", "pi_seed_foodbridge_maya", "For tonight's grocery kits.", false, DonationStatus.SUCCEEDED);
        ensureDonation(theo, greenSteps, "20.00", "pi_seed_greensteps_theo", "For cleanup supplies.", false, DonationStatus.SUCCEEDED);
        ensureDonation(lina, brightFuture, "50.00", "pi_seed_brightfuture_lina", "For student learning materials.", false, DonationStatus.SUCCEEDED);
        ensureDonation(maya, brightFuture, "15.00", "pi_seed_brightfuture_maya", "Small boost for tutoring.", true, DonationStatus.SUCCEEDED);
        ensureDonation(sofia, pawsCare, "42.00", "pi_seed_pawscare_sofia", "For adoption day supplies.", false, DonationStatus.SUCCEEDED);
        ensureDonation(adam, reliefNow, "75.00", "pi_seed_reliefnow_adam", "For emergency kit materials.", false, DonationStatus.PENDING);
        ensureDonation(ines, seniorCircle, "30.00", "pi_seed_seniorcircle_ines", "For senior visit games and tea.", false, DonationStatus.SUCCEEDED);
        ensureDonation(noah, reliefNow, "60.00", "pi_seed_reliefnow_noah", "First-aid restock.", true, DonationStatus.SUCCEEDED);
        ensureDonation(theo, pawsCare, "10.00", "pi_seed_pawscare_theo_failed", "Trying a small donation.", false, DonationStatus.FAILED);
        ensureDonation(lina, foodBridge, "25.00", "pi_seed_foodbridge_lina_refunded", "Duplicate donation refund example.", false, DonationStatus.REFUNDED);
    }

    private void ensureDonation(UserEntity donor, Organization organization, String amount, String paymentIntentId,
                                String message, boolean anonymous, DonationStatus status) {
        Donation donation = donationRepository.findByStripePaymentIntentId(paymentIntentId).orElseGet(Donation::new);
        donation.setDonor(donor);
        donation.setOrganization(organization);
        donation.setAmount(new BigDecimal(amount));
        donation.setCurrency("EUR");
        donation.setStatus(status);
        donation.setStripePaymentIntentId(paymentIntentId);
        donation.setMessage(message);
        donation.setAnonymous(anonymous);
        donationRepository.save(donation);
    }

    private void ensureNotifications(Volunteer maya, Volunteer theo, Volunteer lina, Volunteer sofia, Volunteer noah,
                                     Organization foodBridge, Organization greenSteps, Organization brightFuture,
                                     Organization pawsCare, Organization reliefNow) {
        ensureNotification(maya, foodBridge.getId(), " applied to your opportunity.", "APPLICATION", "/dashboard", false);
        ensureNotification(theo, greenSteps.getId(), " applied to your opportunity.", "APPLICATION", "/dashboard", true);
        ensureNotification(brightFuture, lina.getId(), " accepted your application.", "APPLICATION_DECISION", "/dashboard", false);
        ensureNotification(foodBridge, maya.getId(), " sent you a message.", "MESSAGE", "/messages", false);
        ensureNotification(sofia, pawsCare.getId(), " donated to your organization.", "DONATION", "/dashboard", true);
        ensureNotification(noah, reliefNow.getId(), " commented on your post.", "COMMENT", "/dashboard", false);
    }

    private void ensureNotification(UserEntity actor, Long recipient, String message, String type, String link, boolean read) {
        boolean exists = notificationRepository.existsSeedNotification(actor.getId(), recipient, message, type);
        if (!exists) {
            Notification notification = new Notification();
            notification.setUser(actor);
            notification.setRecipient(recipient);
            notification.setMessage(message);
            notification.setNotificationType(type);
            notification.setLink(link);
            notification.setIsRead(read);
            notificationRepository.save(notification);
        }
    }

    private boolean sameId(UserEntity first, UserEntity second) {
        return first != null && second != null && first.getId() != null && first.getId().equals(second.getId());
    }

    private boolean sameId(Post first, Post second) {
        return first != null && second != null && first.getId() != null && first.getId().equals(second.getId());
    }
}
