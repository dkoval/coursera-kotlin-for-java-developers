package taxipark

import org.junit.Assert
import org.junit.Test

class TestTaxiPark {
    @Test
    fun testFakeDrivers() {
        val tp = taxiPark(1..3, 1..2, trip(1, 1), trip(1, 2))
        Assert.assertEquals("Wrong result for 'findFakeDrivers()'." + tp.display(),
                drivers(2, 3).toSet(), tp.findFakeDrivers())
    }

    @Test
    fun testFaithfulPassengers() {
        val tp = taxiPark(1..3, 1..5, trip(1, 1), trip(2, 1), trip(1, 4), trip(3, 4), trip(1, 5), trip(2, 5), trip(2, 2))
        Assert.assertEquals("Wrong result for 'findFaithfulPassengers()'. MinTrips: 2." + tp.display(),
                passengers(1, 4, 5), tp.findFaithfulPassengers(2))
    }

    @Test
    fun testFrequentPassengers() {
        val tp = taxiPark(1..2, 1..4, trip(1, 1), trip(1, 1), trip(1, listOf(1, 3)), trip(1, 3), trip(1, 2), trip(2, 2))
        Assert.assertEquals("Wrong result for 'findFrequentPassengers()'. Driver: ${driver(1).name}." + tp.display(),
                passengers(1, 3), tp.findFrequentPassengers(driver(1)))
    }

    @Test
    fun testSmartPassengers() {
        val tp = taxiPark(1..2, 1..2, trip(1, 1, discount = 0.1), trip(2, 2))
        Assert.assertEquals("Wrong result for 'findSmartPassengers()'." + tp.display(),
                passengers(1), tp.findSmartPassengers())
    }

    @Test
    fun testTheMostFrequentTripDuration() {
        val tp = taxiPark(1..3, 1..5, trip(1, 1, duration = 10), trip(3, 4, duration = 30),
                trip(1, 2, duration = 20), trip(2, 3, duration = 35))
        Assert.assertEquals("Wrong result for 'findTheMostFrequentTripDurationPeriod()'.",
                30..39, tp.findTheMostFrequentTripDurationPeriod())
    }

    @Test
    fun testParetoPrincipleSucceeds() {
        val tp = taxiPark(1..5, 1..4,
                trip(1, 1, 20, 20.0),
                trip(1, 2, 20, 20.0),
                trip(1, 3, 20, 20.0),
                trip(1, 4, 20, 20.0),
                trip(2, 1, 20, 20.0))
        Assert.assertEquals(
                "Wrong result for 'checkParetoPrinciple()'." + tp.display(),
                true, tp.checkParetoPrinciple())
    }

    @Test
    fun testParetoPrincipleFails() {
        val tp = taxiPark(1..5, 1..4,
                trip(1, 1, 20, 20.0),
                trip(1, 2, 20, 20.0),
                trip(1, 3, 20, 20.0),
                trip(2, 4, 20, 20.0),
                trip(3, 1, 20, 20.0))
        Assert.assertEquals(
                "Wrong result for 'checkParetoPrinciple()'." + tp.display(),
                false, tp.checkParetoPrinciple())
    }

    @Test
    fun testParetoPrincipleNoTrips() {
        val tp = taxiPark(1..5, 1..4)
        Assert.assertEquals(
                "Wrong result for 'checkParetoPrinciple()'." + tp.display(),
                false, tp.checkParetoPrinciple())
    }
}