package com.navneetgupta

import cats.Monad
import cats.data.EitherK

object Events {

}

object EventsourcedBooking {

  def behavior[F[_]: Monad]: EventsourcedBehavior[
    EitherK[Booking, BookingCommandRejection, ?[_]],
    F,
    Option[BookingState],
    BookingEvent
    ] =
    EventsourcedBehavior
      .optionalRejectable(new EventsourcedBooking(), BookingState.init, _.handleEvent(_))
}
