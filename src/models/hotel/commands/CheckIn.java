package models.hotel.commands;

import exceptions.reservations.ReservationAlreadyExistsException;
import exceptions.reservations.ReservationException;
import exceptions.rooms.RoomException;
import models.Hotel;
import models.hotel.commands.contracts.HotelCommand;
import models.reservations.Reservation;
import models.rooms.Room;

import java.time.LocalDate;
import java.util.List;

public class CheckIn implements HotelCommand {
    private final long id;
    private final LocalDate from;
    private final LocalDate to;
    private final String note;
    private final int argumentSize;
    private int numberOfGuests;
    private final List<Room> hotelRooms;


    public CheckIn(List<String> arguments, Hotel hotel) {
        this.hotelRooms = hotel.getHotelRooms();
        this.id = Long.parseLong(arguments.get(0));
        this.from = LocalDate.parse(arguments.get(1));
        this.to = LocalDate.parse(arguments.get(2));
        this.note = arguments.get(3);
        this.argumentSize = arguments.size();

        if(argumentSize == 5){
            numberOfGuests = Integer.parseInt(arguments.get(4));
        }
    }

    @Override
    public void execute() throws ReservationException, RoomException {
        for (Room room : this.hotelRooms) {
            if (room.getNumber() == id) {
                if (argumentSize != 5) {
                    numberOfGuests = room.getNumberOfBeds();
                } else if (numberOfGuests > room.getNumberOfBeds()) {
                    throw new RoomException("Not enough beds in the room");
                }

                if (from.isBefore(LocalDate.now())) {
                    throw new ReservationException("Can't make a reservation on a past date!");
                }

                Reservation lastReservation = room.getLastReservation();
                if (lastReservation != null && lastReservation.getEndDate().equals(from)) {
                    throw new ReservationException("Can't make a reservation for consecutive dates");
                }

                if (room.checkRoomIfAvailable(from, to)) {
                    room.addReservation(new Reservation(from, to, note, numberOfGuests));
                } else {
                    throw new ReservationAlreadyExistsException("Can't make a reservation, one already exists!");
                }

                System.out.println("Successfully made the reservation!");

                break;
            }
        }

//        Регистриране в стая с номер <room> от дата <from> до дата <to> и се добавя бележка <note>.
//        Незадължителният параметър <guests> задава броя на гостите, настанени в стаята.
//        Ако той не е указан, се счита, че броят на настанените гости е равен на броя на леглата в стаята.
//        Пример: checkin 229 2020-03-23 2020-03-31 The Simpsons
    }
}
