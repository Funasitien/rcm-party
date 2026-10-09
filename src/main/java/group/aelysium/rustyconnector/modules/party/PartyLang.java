package group.aelysium.rustyconnector.modules.party;

import group.aelysium.rustyconnector.common.lang.Lang;
import net.kyori.adventure.text.Component;
import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.JoinConfiguration.*;
import static net.kyori.adventure.text.format.NamedTextColor.BLUE;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.DARK_GRAY;
import net.kyori.adventure.text.format.TextColor;

public class PartyLang {
    private static final TextColor ERROR_COLOR = TextColor.color(0xC21833);
    private static final TextColor WARN_COLOR = TextColor.color(0xFFDFBA);
    private static final TextColor SUCCESS_COLOR = TextColor.color(0xBAFFC9);
    private static final TextColor PRIMARY_COLOR = BLUE;
    private static final TextColor SECONDARY_COLOR = TextColor.color(0xFFDFBA);
    private static final TextColor ACCENT_COLOR = TextColor.color(0xE6B3FF);
    private static final TextColor OFFLINE_COLOR = TextColor.color(0xD3D3D3);

    @Lang("rcm-party-created")
    public static Component partyCreated() {
        return text("Party created!", SUCCESS_COLOR);
    }
    
    @Lang("rcm-party-left")
    public static Component partyLeft() {
        return text("You left the party.", WARN_COLOR);
    }

    @Lang("rcm-party-invited")
    public static Component partyInvited(String target) {
        return text("Invited ", SUCCESS_COLOR).append(text(target, ACCENT_COLOR)).append(text(" to the party.", SUCCESS_COLOR));
    }
    
    @Lang("rcm-party-received-invite")
    public static Component partyReceivedInvite(String sender) {
        return text("You have been invited to a party by ", SUCCESS_COLOR).append(text(sender, ACCENT_COLOR)).append(text(".", SUCCESS_COLOR));
    }

    @Lang("rcm-party-not-in-party")
    public static Component notInParty() {
        return text("You are not in a party.", ERROR_COLOR);
    }

    @Lang("rcm-party-already-in-party")
    public static Component alreadyInParty() {
        return text("You are already in a party.", ERROR_COLOR);
    }

    @Lang("rcm-party-only-leader-invite")
    public static Component onlyLeaderInvite() {
        return text("Only the party leader can invite players.", ERROR_COLOR);
    }

    @Lang("rcm-party-auto-follow-toggled")
    public static Component autoFollowToggled(boolean enabled) {
        return text("Party auto-follow is now ", PRIMARY_COLOR)
                .append(text((enabled ? "enabled" : "disabled"), enabled ? SUCCESS_COLOR : ERROR_COLOR))
                .append(text(".", PRIMARY_COLOR));
    }

    @Lang("rcm-party-teleporting")
    public static Component teleporting() {
        return text("Teleporting to the party leader's server...", PRIMARY_COLOR);
    }
    
    @Lang("rcm-party-disbanded")
    public static Component partyDisbanded() {
        return text("The party has been disbanded.", ERROR_COLOR);
    }

    @Lang("rcm-party-usage-invite")
    public static Component usageInvite() {
        return text("Usage: /party invite <player>", ERROR_COLOR);
    }
}
