package com.rickiey_innovates.juditonspringapp.models;

import java.time.LocalDate;
import java.util.List;

public class UpdateMember {
    private MainDetails mainDetails;
    private SpouseDetails spouse;
    private ChildrenDetails childrenDetails;

    public MainDetails getMainDetails() {
        return mainDetails;
    }

    public void setMainDetails(MainDetails mainDetails) {
        this.mainDetails = mainDetails;
    }

    public SpouseDetails getSpouse() {
        return spouse;
    }

    public void setSpouse(SpouseDetails spouse) {
        this.spouse = spouse;
    }

    public ChildrenDetails getChildrenDetails() {
        return childrenDetails;
    }

    public void setChildrenDetails(ChildrenDetails childrenDetails) {
        this.childrenDetails = childrenDetails;
    }

    public static class MainDetails {

        private int id;
        private String image;
        private String first;
        private String second;
        private String last;
        private String dob;
        private String gender;
        private String isMember;
        private String membershipDuration, memberGroup, enrolmentDate, enrolmentPlace;
        private String phone;
        private String email;

        private String address;
        private String residence;
        private String zone;
        private String preferredService;
        private String profession;
        private String occupation;
        private String employer;
        private String baptised;
        private String baptismDate;
        private String placeOfBaptism;
        private String baptisedBy;
        private String confirmed;
        private String confirmationDate;
        private String placeOfConfirmation;
        private String confirmedBy;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public String getSecond() {
            return second;
        }

        public void setSecond(String second) {
            this.second = second;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getMemberGroup() {
            return memberGroup;
        }

        public void setMemberGroup(String memberGroup) {
            this.memberGroup = memberGroup;
        }

        public String getIsMember() {
            return isMember;
        }

        public void setIsMember(String isMember) {
            this.isMember = isMember;
        }

        public String getMembershipDuration() {
            return membershipDuration;
        }

        public void setMembershipDuration(String membershipDuration) {
            this.membershipDuration = membershipDuration;
        }

        public String getEnrolmentDate() {
            return enrolmentDate;
        }

        public void setEnrolmentDate(String enrolmentDate) {
            this.enrolmentDate = enrolmentDate;
        }

        public String getEnrolmentPlace() {
            return enrolmentPlace;
        }

        public void setEnrolmentPlace(String enrolmentPlace) {
            this.enrolmentPlace = enrolmentPlace;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getResidence() {
            return residence;
        }

        public void setResidence(String residence) {
            this.residence = residence;
        }

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }

        public String getPreferredService() {
            return preferredService;
        }

        public void setPreferredService(String preferredService) {
            this.preferredService = preferredService;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getOccupation() {
            return occupation;
        }

        public void setOccupation(String occupation) {
            this.occupation = occupation;
        }

        public String getEmployer() {
            return employer;
        }

        public void setEmployer(String employer) {
            this.employer = employer;
        }

        public String getBaptised() {
            return baptised;
        }

        public void setBaptised(String baptised) {
            this.baptised = baptised;
        }

        public String getBaptismDate() {
            return baptismDate;
        }

        public void setBaptismDate(String baptismDate) {
            this.baptismDate = baptismDate;
        }

        public String getPlaceOfBaptism() {
            return placeOfBaptism;
        }

        public void setPlaceOfBaptism(String placeOfBaptism) {
            this.placeOfBaptism = placeOfBaptism;
        }

        public String getBaptisedBy() {
            return baptisedBy;
        }

        public void setBaptisedBy(String baptisedBy) {
            this.baptisedBy = baptisedBy;
        }

        public String getConfirmed() {
            return confirmed;
        }

        public void setConfirmed(String confirmed) {
            this.confirmed = confirmed;
        }

        public String getConfirmationDate() {
            return confirmationDate;
        }

        public void setConfirmationDate(String confirmationDate) {
            this.confirmationDate = confirmationDate;
        }

        public String getPlaceOfConfirmation() {
            return placeOfConfirmation;
        }

        public void setPlaceOfConfirmation(String placeOfConfirmation) {
            this.placeOfConfirmation = placeOfConfirmation;
        }

        public String getConfirmedBy() {
            return confirmedBy;
        }

        public void setConfirmedBy(String confirmedBy) {
            this.confirmedBy = confirmedBy;
        }

        @Override
        public String toString() {
            return "MainDetails{" +
                    "id=" + id +
                    ", image='" + image + '\'' +
                    ", first='" + first + '\'' +
                    ", second='" + second + '\'' +
                    ", last='" + last + '\'' +
                    ", dob='" + dob + '\'' +
                    ", gender='" + gender + '\'' +
                    ", isMember=" + isMember +
                    ", membershipDuration='" + membershipDuration + '\'' +
                    ", memberGroup='" + memberGroup + '\'' +
                    ", enrolmentDate='" + enrolmentDate + '\'' +
                    ", enrolmentPlace='" + enrolmentPlace + '\'' +
                    ", phone='" + phone + '\'' +
                    ", email='" + email + '\'' +
                    ", address='" + address + '\'' +
                    ", residence='" + residence + '\'' +
                    ", zone='" + zone + '\'' +
                    ", preferredService='" + preferredService + '\'' +
                    ", profession='" + profession + '\'' +
                    ", occupation='" + occupation + '\'' +
                    ", employer='" + employer + '\'' +
                    ", baptised='" + baptised + '\'' +
                    ", baptismDate='" + baptismDate + '\'' +
                    ", placeOfBaptism='" + placeOfBaptism + '\'' +
                    ", baptisedBy='" + baptisedBy + '\'' +
                    ", confirmed='" + confirmed + '\'' +
                    ", confirmationDate='" + confirmationDate + '\'' +
                    ", placeOfConfirmation='" + placeOfConfirmation + '\'' +
                    ", confirmedBy='" + confirmedBy + '\'' +
                    '}';
        }
    }

    public static class SpouseDetails {
        private int spouseId;
        private String maritalStatus;

        private String marriageType;

        private LocalDate weddingDate;
        private String weddingPlace;

        private String spouseImage;
        private String spouseFName;
        private String spouseSecond;
        private String spouseLast;
        private String spouseDob;
        private String spouseGender;
        private String spouseMemberGroup;
        private String spousePhone;
        private String spouseEmail;
        private String spouseAddress;
        private String spouseResidence;
        private String spouseZone;
        private String spousePreferredService;
        private String spouseProfession;
        private String spouseOccupation;
        private String spouseEmployer;
        private String spouseBaptised;
        private String spouseBaptismDate;
        private String spousePlaceOfBaptism;
        private String spouseBaptisedBy;
        private String spouseConfirmed;
        private String spouseConfirmationDate;
        private String spousePlaceOfConfirmation;
        private String spouseConfirmedBy;

        public int getSpouseId() {
            return spouseId;
        }

        public void setSpouseId(int spouseId) {
            this.spouseId = spouseId;
        }

        public String getMaritalStatus() {
            return maritalStatus;
        }

        public void setMaritalStatus(String maritalStatus) {
            this.maritalStatus = maritalStatus;
        }

        public String getMarriageType() {
            return marriageType;
        }

        public void setMarriageType(String marriageType) {
            this.marriageType = marriageType;
        }

        public LocalDate getWeddingDate() {
            return weddingDate;
        }

        public void setWeddingDate(LocalDate weddingDate) {
            this.weddingDate = weddingDate;
        }

        public String getWeddingPlace() {
            return weddingPlace;
        }

        public void setWeddingPlace(String weddingPlace) {
            this.weddingPlace = weddingPlace;
        }

        public String getSpouseImage() {
            return spouseImage;
        }

        public void setSpouseImage(String spouseImage) {
            this.spouseImage = spouseImage;
        }

        public String getSpouseFName() {
            return spouseFName;
        }

        public void setSpouseFName(String spouseFName) {
            this.spouseFName = spouseFName;
        }

        public String getSpouseSecond() {
            return spouseSecond;
        }

        public void setSpouseSecond(String spouseSecond) {
            this.spouseSecond = spouseSecond;
        }

        public String getSpouseLast() {
            return spouseLast;
        }

        public void setSpouseLast(String spouseLast) {
            this.spouseLast = spouseLast;
        }

        public String getSpouseDob() {
            return spouseDob;
        }

        public void setSpouseDob(String spouseDob) {
            this.spouseDob = spouseDob;
        }

        public String getSpouseGender() {
            return spouseGender;
        }

        public void setSpouseGender(String spouseGender) {
            this.spouseGender = spouseGender;
        }

        public String getSpouseMemberGroup() {
            return spouseMemberGroup;
        }

        public void setSpouseMemberGroup(String spouseMemberGroup) {
            this.spouseMemberGroup = spouseMemberGroup;
        }

        public String getSpousePhone() {
            return spousePhone;
        }

        public void setSpousePhone(String spousePhone) {
            this.spousePhone = spousePhone;
        }

        public String getSpouseEmail() {
            return spouseEmail;
        }

        public void setSpouseEmail(String spouseEmail) {
            this.spouseEmail = spouseEmail;
        }

        public String getSpouseAddress() {
            return spouseAddress;
        }

        public void setSpouseAddress(String spouseAddress) {
            this.spouseAddress = spouseAddress;
        }

        public String getSpouseResidence() {
            return spouseResidence;
        }

        public void setSpouseResidence(String spouseResidence) {
            this.spouseResidence = spouseResidence;
        }

        public String getSpouseZone() {
            return spouseZone;
        }

        public void setSpouseZone(String spouseZone) {
            this.spouseZone = spouseZone;
        }

        public String getSpousePreferredService() {
            return spousePreferredService;
        }

        public void setSpousePreferredService(String spousePreferredService) {
            this.spousePreferredService = spousePreferredService;
        }

        public String getSpouseProfession() {
            return spouseProfession;
        }

        public void setSpouseProfession(String spouseProfession) {
            this.spouseProfession = spouseProfession;
        }

        public String getSpouseOccupation() {
            return spouseOccupation;
        }

        public void setSpouseOccupation(String spouseOccupation) {
            this.spouseOccupation = spouseOccupation;
        }

        public String getSpouseEmployer() {
            return spouseEmployer;
        }

        public void setSpouseEmployer(String spouseEmployer) {
            this.spouseEmployer = spouseEmployer;
        }

        public String getSpouseBaptised() {
            return spouseBaptised;
        }

        public void setSpouseBaptised(String spouseBaptised) {
            this.spouseBaptised = spouseBaptised;
        }

        public String getSpouseBaptismDate() {
            return spouseBaptismDate;
        }

        public void setSpouseBaptismDate(String spouseBaptismDate) {
            this.spouseBaptismDate = spouseBaptismDate;
        }

        public String getSpousePlaceOfBaptism() {
            return spousePlaceOfBaptism;
        }

        public void setSpousePlaceOfBaptism(String spousePlaceOfBaptism) {
            this.spousePlaceOfBaptism = spousePlaceOfBaptism;
        }

        public String getSpouseBaptisedBy() {
            return spouseBaptisedBy;
        }

        public void setSpouseBaptisedBy(String spouseBaptisedBy) {
            this.spouseBaptisedBy = spouseBaptisedBy;
        }

        public String getSpouseConfirmed() {
            return spouseConfirmed;
        }

        public void setSpouseConfirmed(String spouseConfirmed) {
            this.spouseConfirmed = spouseConfirmed;
        }

        public String getSpouseConfirmationDate() {
            return spouseConfirmationDate;
        }

        public void setSpouseConfirmationDate(String spouseConfirmationDate) {
            this.spouseConfirmationDate = spouseConfirmationDate;
        }

        public String getSpousePlaceOfConfirmation() {
            return spousePlaceOfConfirmation;
        }

        public void setSpousePlaceOfConfirmation(String spousePlaceOfConfirmation) {
            this.spousePlaceOfConfirmation = spousePlaceOfConfirmation;
        }

        public String getSpouseConfirmedBy() {
            return spouseConfirmedBy;
        }

        public void setSpouseConfirmedBy(String spouseConfirmedBy) {
            this.spouseConfirmedBy = spouseConfirmedBy;
        }

        @Override
        public String toString() {
            return "SpouseDetails{" +
                    "spouseId=" + spouseId +
                    ", maritalStatus='" + maritalStatus + '\'' +
                    ", marriageType='" + marriageType + '\'' +
                    ", weddingDate=" + weddingDate +
                    ", weddingPlace='" + weddingPlace + '\'' +
                    ", spouseImage='" + spouseImage + '\'' +
                    ", spouseFName='" + spouseFName + '\'' +
                    ", spouseSecond='" + spouseSecond + '\'' +
                    ", spouseLast='" + spouseLast + '\'' +
                    ", spouseDob='" + spouseDob + '\'' +
                    ", spouseGender='" + spouseGender + '\'' +
                    ", spouseMemberGroup='" + spouseMemberGroup + '\'' +
                    ", spousePhone='" + spousePhone + '\'' +
                    ", spouseEmail='" + spouseEmail + '\'' +
                    ", spouseAddress='" + spouseAddress + '\'' +
                    ", spouseResidence='" + spouseResidence + '\'' +
                    ", spouseZone='" + spouseZone + '\'' +
                    ", spousePreferredService='" + spousePreferredService + '\'' +
                    ", spouseProfession='" + spouseProfession + '\'' +
                    ", spouseOccupation='" + spouseOccupation + '\'' +
                    ", spouseEmployer='" + spouseEmployer + '\'' +
                    ", spouseBaptised='" + spouseBaptised + '\'' +
                    ", spouseBaptismDate='" + spouseBaptismDate + '\'' +
                    ", spousePlaceOfBaptism='" + spousePlaceOfBaptism + '\'' +
                    ", spouseBaptisedBy='" + spouseBaptisedBy + '\'' +
                    ", spouseConfirmed='" + spouseConfirmed + '\'' +
                    ", spouseConfirmationDate='" + spouseConfirmationDate + '\'' +
                    ", spousePlaceOfConfirmation='" + spousePlaceOfConfirmation + '\'' +
                    ", spouseConfirmedBy='" + spouseConfirmedBy + '\'' +
                    '}';
        }
    }

    public static class ChildrenDetails {
        private String numberOfChildren;
        private List<Child> children;

        public String getNumberOfChildren() {
            return numberOfChildren;
        }

        public void setNumberOfChildren(String numberOfChildren) {
            this.numberOfChildren = numberOfChildren;
        }

        public List<Child> getChildren() {
            return children;
        }

        public void setChildren(List<Child> children) {
            this.children = children;
        }

        public static class Child {

            private int childId;
            private String childImage;
            private String childFName;
            private String childMName;
            private String childLName;
            private String childDob;
            private String childGender;
            private String childMemberGroup;
            private String childPhone;
            private String childEmail;
            private String childAddress;
            private String childResidence;
            private String childZone;
            private String childPreferredService;
            private String childProfession;
            private String childOccupation;
            private String childEmployer;
            private String childBaptised;
            private String childBaptismDate;
            private String childPlaceOfBaptism;
            private String childBaptisedBy;
            private String childConfirmed;
            private String childConfirmationDate;
            private String childPlaceOfConfirmation;
            private String childConfirmedBy;

            public int getChildId() {
                return childId;
            }

            public void setChildId(int childId) {
                this.childId = childId;
            }

            public String getChildImage() {
                return childImage;
            }

            public void setChildImage(String childImage) {
                this.childImage = childImage;
            }

            public String getChildFName() {
                return childFName;
            }

            public void setChildFName(String childFName) {
                this.childFName = childFName;
            }

            public String getChildMName() {
                return childMName;
            }

            public void setChildMName(String childMName) {
                this.childMName = childMName;
            }

            public String getChildLName() {
                return childLName;
            }

            public void setChildLName(String childLName) {
                this.childLName = childLName;
            }

            public String getChildDob() {
                return childDob;
            }

            public void setChildDob(String childDob) {
                this.childDob = childDob;
            }

            public String getChildGender() {
                return childGender;
            }

            public void setChildGender(String childGender) {
                this.childGender = childGender;
            }

            public String getChildMemberGroup() {
                return childMemberGroup;
            }

            public void setChildMemberGroup(String childMemberGroup) {
                this.childMemberGroup = childMemberGroup;
            }

            public String getChildPhone() {
                return childPhone;
            }

            public void setChildPhone(String childPhone) {
                this.childPhone = childPhone;
            }

            public String getChildEmail() {
                return childEmail;
            }

            public void setChildEmail(String childEmail) {
                this.childEmail = childEmail;
            }

            public String getChildAddress() {
                return childAddress;
            }

            public void setChildAddress(String childAddress) {
                this.childAddress = childAddress;
            }

            public String getChildResidence() {
                return childResidence;
            }

            public void setChildResidence(String childResidence) {
                this.childResidence = childResidence;
            }

            public String getChildZone() {
                return childZone;
            }

            public void setChildZone(String childZone) {
                this.childZone = childZone;
            }

            public String getChildPreferredService() {
                return childPreferredService;
            }

            public void setChildPreferredService(String childPreferredService) {
                this.childPreferredService = childPreferredService;
            }

            public String getChildProfession() {
                return childProfession;
            }

            public void setChildProfession(String childProfession) {
                this.childProfession = childProfession;
            }

            public String getChildOccupation() {
                return childOccupation;
            }

            public void setChildOccupation(String childOccupation) {
                this.childOccupation = childOccupation;
            }

            public String getChildEmployer() {
                return childEmployer;
            }

            public void setChildEmployer(String childEmployer) {
                this.childEmployer = childEmployer;
            }

            public String getChildBaptised() {
                return childBaptised;
            }

            public void setChildBaptised(String childBaptised) {
                this.childBaptised = childBaptised;
            }

            public String getChildBaptismDate() {
                return childBaptismDate;
            }

            public void setChildBaptismDate(String childBaptismDate) {
                this.childBaptismDate = childBaptismDate;
            }

            public String getChildPlaceOfBaptism() {
                return childPlaceOfBaptism;
            }

            public void setChildPlaceOfBaptism(String childPlaceOfBaptism) {
                this.childPlaceOfBaptism = childPlaceOfBaptism;
            }

            public String getChildBaptisedBy() {
                return childBaptisedBy;
            }

            public void setChildBaptisedBy(String childBaptisedBy) {
                this.childBaptisedBy = childBaptisedBy;
            }

            public String getChildConfirmed() {
                return childConfirmed;
            }

            public void setChildConfirmed(String childConfirmed) {
                this.childConfirmed = childConfirmed;
            }

            public String getChildConfirmationDate() {
                return childConfirmationDate;
            }

            public void setChildConfirmationDate(String childConfirmationDate) {
                this.childConfirmationDate = childConfirmationDate;
            }

            public String getChildPlaceOfConfirmation() {
                return childPlaceOfConfirmation;
            }

            public void setChildPlaceOfConfirmation(String childPlaceOfConfirmation) {
                this.childPlaceOfConfirmation = childPlaceOfConfirmation;
            }

            public String getChildConfirmedBy() {
                return childConfirmedBy;
            }

            public void setChildConfirmedBy(String childConfirmedBy) {
                this.childConfirmedBy = childConfirmedBy;
            }

            @Override
            public String toString() {
                return "Child{" +
                        "childId=" + childId +
                        ", childImage='" + childImage + '\'' +
                        ", childFName='" + childFName + '\'' +
                        ", childMName='" + childMName + '\'' +
                        ", childLName='" + childLName + '\'' +
                        ", childDob='" + childDob + '\'' +
                        ", childGender='" + childGender + '\'' +
                        ", childMemberGroup='" + childMemberGroup + '\'' +
                        ", childPhone='" + childPhone + '\'' +
                        ", childEmail='" + childEmail + '\'' +
                        ", childAddress='" + childAddress + '\'' +
                        ", childResidence='" + childResidence + '\'' +
                        ", childZone='" + childZone + '\'' +
                        ", childPreferredService='" + childPreferredService + '\'' +
                        ", childProfession='" + childProfession + '\'' +
                        ", childOccupation='" + childOccupation + '\'' +
                        ", childEmployer='" + childEmployer + '\'' +
                        ", childBaptised='" + childBaptised + '\'' +
                        ", childBaptismDate='" + childBaptismDate + '\'' +
                        ", childPlaceOfBaptism='" + childPlaceOfBaptism + '\'' +
                        ", childBaptisedBy='" + childBaptisedBy + '\'' +
                        ", childConfirmed='" + childConfirmed + '\'' +
                        ", childConfirmationDate='" + childConfirmationDate + '\'' +
                        ", childPlaceOfConfirmation='" + childPlaceOfConfirmation + '\'' +
                        ", childConfirmedBy='" + childConfirmedBy + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ChildrenDetails{" +
                    "numberOfChildren='" + numberOfChildren + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UpdateMember{" +
                "mainDetails=" + mainDetails +
                ", spouse=" + spouse +
                ", childrenDetails=" + childrenDetails +
                '}';
    }
}

