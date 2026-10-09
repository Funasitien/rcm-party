package group.aelysium.rustyconnector.modules.party;

import group.aelysium.rustyconnector.proxy.player.Player;
import group.aelysium.rustyconnector.shaded.group.aelysium.declarative_yaml.DeclarativeYAML;
import group.aelysium.rustyconnector.shaded.group.aelysium.declarative_yaml.annotations.*;
import group.aelysium.rustyconnector.shaded.group.aelysium.declarative_yaml.lib.Printer;
import group.aelysium.rustyconnector.shaded.group.aelysium.declarative_yaml.lib.Serializable;

@Namespace("rustyconnector-modules")
@Config("/rcm-party/config.yml")
@Comment({
        "###########################################################################################################",
        "#|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||#",
        "###########################################################################################################",
        "#                                                                                                         #",
        "#                    ______   ______     ______     ______   __     ______     ______                     #",
        "#                   /\\  == \\ /\\  __ \\   /\\  == \\   /\\__  _\\ /\\ \\   /\\  ___\\   /\\  ___\\                    #",
        "#                   \\ \\  _-/ \\ \\  __ \\  \\ \\  __<   \\/_/\\ \\/ \\ \\ \\  \\ \\  __\\   \\ \\___  \\                   #",
        "#                    \\ \\_\\    \\ \\_\\ \\_\\  \\ \\_\\ \\_\\    \\ \\_\\  \\ \\_\\  \\ \\_____\\  \\/\\_____\\                  #",
        "#                     \\/_/     \\/_/\\/_/   \\/_/ /_/     \\/_/   \\/_/   \\/_____/   \\/_____/                  #",
        "#                                                                                                         #",
        "#                                                                                                         #",
        "#                                            Welcome to Parties!                                          #",
        "#                                                                                                         #",
        "#                            -------------------------------------------------                            #",
        "#                                                                                                         #",
        "#                        | Allow your users to teleport around your network together                      #",
        "#                        | in parties!                                                                    #",
        "#                                                                                                         #",
        "#                            -------------------------------------------------                            #",
        "#                                                                                                         #",
        "###########################################################################################################",
        "#|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||#",
        "###########################################################################################################"
})
public class PartyConfig {
    @Node(0)
    public int maxMember = 5;
    @Node(1)
    public boolean friendsOnly = false;
    @Node(2)
    public boolean localOnly = true;

    @Node(3)
    @Comment("The haze database to use")
    public String database = "mysql";

    @Node(4)
    public PartyLeader partyLeader = new PartyLeader();

    @Node(5)
    public SwitchingServers switchingServers = new SwitchingServers();

    public static class PartyLeader extends Serializable {
        @Node(0)
        public boolean onlyLeaderCanInvite = true;
        @Node(1)
        public boolean onlyLeaderCanKick = true;
        @Node(2)
        public boolean onlyLeaderCanSwitch = true;
        @Node(3)
        public boolean disbandOnLeaderQuit = true;
    }

    public static class SwitchingServers extends Serializable {
        @Node(0)
        public Player.Connection.Power switchPower = Player.Connection.Power.MODERATE;
        @Node(1)
        public PartyRegistry.OverflowHandler overflowHandler = PartyRegistry.OverflowHandler.BLOCK_SWITCH;
    }

    public static PartyConfig New() {
        return DeclarativeYAML.From(PartyConfig.class, new Printer());
    }
}
