package CounterStriker.models.players;

import CounterStriker.common.ExceptionMessages;
import CounterStriker.models.guns.Gun;

public abstract class PlayerImpl implements Player {
    private String username;
    private int health;
    private int armor;
    private boolean isAlive;
    private Gun gun;

    protected PlayerImpl(String username, int health, int armor, Gun gun) {
        setUsername(username);
        setHealth(health);
        setArmor(armor);
        setGun(gun);
    }

    private void setUsername(String username) {

        if (username == null || username.trim().isEmpty()) {
            throw new NullPointerException(ExceptionMessages.INVALID_PLAYER_NAME);
        }
        this.username = username;
    }

    private void setHealth(int health) {

        if (health < 0) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_PLAYER_HEALTH);
        }
        this.health = health;
    }

    private void setArmor(int armor) {

        if (armor < 0) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_PLAYER_ARMOR);
        }
        this.armor = armor;
    }

    private void setGun(Gun gun) {

        if (gun == null) {
            throw new NullPointerException(ExceptionMessages.INVALID_GUN);
        }
        this.gun = gun;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getArmor() {
        return armor;
    }

    @Override
    public Gun getGun() {
        return gun;
    }

    @Override
    public boolean isAlive() {
        return isAlive = health > 0;
    }

    @Override
    public void takeDamage(int points) {
        int prevArmor = armor;
        armor -= points;

        if (armor < 0) {
            armor = 0;
            health -= points - prevArmor;

            if (health < 0) {
                health = 0;
            }
        }
    }

    public String toString() {
        StringBuilder output = new StringBuilder();

        output.append(this.getClass().getSimpleName()).append(": ").append(username)
                .append(System.lineSeparator()).append("--Health: ").append(health).append(System.lineSeparator())
                .append("--Armor: ").append(armor).append(System.lineSeparator())
                .append("--Gun: ").append(gun.getName()).append(System.lineSeparator());

        return output.toString();
    }
}
