package com.arena.ticketing.model;

public enum MatchStatus {
    SCHEDULED,   // Meciul este programat, se pot vinde bilete
    CANCELLED,   // Meciul a fost anulat, vânzarea se oprește
    FINISHED     // Meciul s-a terminat
}