package app;

/**
 * @author Wojciech Sankowski
 *
 * represents database keys for Component table
 */
public enum Component {
    C6H6("c6h6"), CO("co"), NO2("no2"), O3("o3"),
    SO2("so2"), PM10("pm10"), PM25("pm2.5");

    /**
     * database key for single component
     */
    private final String name;

    Component(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}