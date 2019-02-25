package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
    allDrivers.subtract(
        trips.map { it.driver }.toSet()
    )

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
    if (minTrips == 0) allPassengers
    else trips.flatMap { trip -> trip.passengers.map { passenger -> passenger to trip } }
        .groupBy { it.first }
        .filter { (_, trips) -> trips.size >= minTrips }
        .keys

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    trips.filter { trip -> trip.driver == driver }
        .flatMap { trip -> trip.passengers.map { passenger -> passenger to trip.driver } }
        .groupBy { it.first }
        .filter { (_, drivers) -> drivers.size > 1 }
        .keys

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
    trips.flatMap { trip -> trip.passengers.map { passenger -> passenger to trip } }
        .groupBy { it.first }
        .mapValues { (_, list) -> list.map { it.second } }
        .mapValues { (_, list) -> list.partition { trip -> (trip.discount ?: 0.0) > 0.0 } }
        .filter { (_, pair) -> pair.first.size > pair.second.size }
        .keys

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val bucket = trips.map { it.duration }
        .groupBy { it / 10 }
        .maxBy { (_, durations) -> durations.size }
        ?.key

    return bucket?.let {
        val start = it * 10
        val end = start + 9
        IntRange(start, end)
    }
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean =
    if (trips.isEmpty()) false
    else {
        val descSortedIncomeByDriver = trips.groupBy { it.driver }
            .mapValues { (_, trips) -> trips.sumByDouble { it.cost } }
            .entries
            .sortedByDescending { it.value }

        val pareto20Threshold = allDrivers.size / 5                 // 20% of the drivers
        val pareto80Threshold = trips.sumByDouble { it.cost } * 0.8 // 80% of the income
        descSortedIncomeByDriver.take(pareto20Threshold).sumByDouble { it.value } >= pareto80Threshold
    }
