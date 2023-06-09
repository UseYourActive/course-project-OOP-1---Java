package models.reservations;

import exceptions.reservations.NotAValidReservationDateRangeException;
import models.reservations.enums.ReservationStatus;
import utils.adapters.LocalDateAdapter;
import utils.adapters.ReservationStatusAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@XmlAccessorType(XmlAccessType.FIELD)
public class Reservation {
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlElement(name = "startDate")
    private LocalDate startDate;
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    @XmlElement(name = "endDate")
    private LocalDate endDate;
    @XmlElement(name = "note")
    private String note;
    @XmlElement(name = "numberOfGuests")
    private int numberOfGuests;
    @XmlJavaTypeAdapter(ReservationStatusAdapter.class)
    @XmlAttribute(name = "status")
    private ReservationStatus status;

    @SuppressWarnings("All")
    private Reservation() {
        this.status = ReservationStatus.NONE;
    }

    public Reservation(LocalDate startDate, LocalDate endDate, String note, int numberOfGuests) throws NotAValidReservationDateRangeException {
        if (startDate.isAfter(endDate)) {
            throw new NotAValidReservationDateRangeException("Unavailable booking");
        }
        this.startDate = startDate;
        this.endDate = endDate;
        this.note = note;
        this.numberOfGuests = numberOfGuests;
        this.status = defineStatus();
    }

    public ReservationStatus defineStatus() {
        if (LocalDate.now().isBefore(startDate)) {
            return ReservationStatus.FUTURE;
        } else if (LocalDate.now().isAfter(endDate)) {
            return ReservationStatus.PAST;
        } else {
            return ReservationStatus.CURRENT;
        }
    }

    @SuppressWarnings("All")
    public boolean checkIfReservationBusy(LocalDate from, LocalDate to) {
        return this.getStartDate().compareTo(from) >= 0 && this.getEndDate().compareTo(to) <= 0;
    }

    @SuppressWarnings("All")
    public boolean checkCompatability(LocalDate from, LocalDate to) {
        LocalDate startDate = this.getStartDate();
        LocalDate endDate = this.getEndDate();

        if (from.isEqual(startDate) || to.isEqual(endDate)) {
            return false;
        }

        if (from.isAfter(startDate) && from.isBefore(endDate)) {
            return false;
        }

        if (to.isAfter(startDate) && to.isBefore(endDate)) {
            return false;
        }

        return !from.isBefore(startDate) || !to.isAfter(endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reservation that = (Reservation) o;

        if (!startDate.equals(that.startDate)) return false;
        if (!endDate.equals(that.endDate)) return false;
        return status == that.status;
    }

    @Override
    public int hashCode() {
        int result = startDate.hashCode();
        result = 31 * result + endDate.hashCode();
        result = 31 * result + status.hashCode();
        return result;
    }

    @SuppressWarnings("All")
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("start date ").append(" end date ").append(" note ").append(" number of guests ").append(" status ").append("\n");
        stringBuilder.append(this.startDate).append(" ").append(this.endDate).append(" ").append(this.note).append(" ").append(this.numberOfGuests).append(" ").append(this.status).append("\n");

        return String.valueOf(stringBuilder);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getNote() {
        return note;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
