package group.aelysium.rustyconnector.modules.party.commands;

import group.aelysium.rustyconnector.RC;
import group.aelysium.rustyconnector.common.errors.Error;
import group.aelysium.rustyconnector.common.util.CommandClient;
import group.aelysium.rustyconnector.modules.party.Party;
import group.aelysium.rustyconnector.modules.party.PartyRegistry;
import group.aelysium.rustyconnector.proxy.player.Player;
import net.kyori.adventure.text.Component;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.SuggestionProvider;
import java.util.UUID;
import java.util.stream.Collectors;

public class PartyCommand {

    public static void register(CommandManager<CommandClient> manager) {
        manager.command(manager.commandBuilder("party")
                .literal("create")
                .handler(context -> {
                    try {
                        CommandClient client = context.sender();
                        if (!(client instanceof CommandClient.Player playerClient)) {
                            client.send(Component.text("Only players can create a party."));
                            return;
                        }

                        Player player = RC.P.Players().fetchByID(playerClient.id()).orElse(null);
                        if (player == null) return;

                        PartyRegistry registry = RC.Module("PartyRegistry");
                        if (registry.contains(UUID.fromString(player.id()))) {
                            player.message(RC.Lang().lang("rcm-party-already-in-party").generate());
                            return;
                        }

                        registry.startParty(player, player.server().orElseThrow());
                        player.message(RC.Lang().lang("rcm-party-created").generate());
                    } catch (Exception e) {
                        RC.Error(Error.from(e).whileAttempting("To execute /party create"));
                    }
                })
        );

        manager.command(manager.commandBuilder("party")
                .literal("leave")
                .handler(context -> {
                    try {
                        CommandClient client = context.sender();
                        if (!(client instanceof CommandClient.Player playerClient)) return;

                        Player player = RC.P.Players().fetchByID(playerClient.id()).orElse(null);
                        if (player == null) return;

                        PartyRegistry registry = RC.Module("PartyRegistry");
                        Party party = registry.fetch(UUID.fromString(player.id())).orElse(null);
                        
                        if (party == null) {
                            player.message(RC.Lang().lang("rcm-party-not-in-party").generate());
                            return;
                        }

                        party.leave(UUID.fromString(player.id()));
                        player.message(RC.Lang().lang("rcm-party-left").generate());
                    } catch (Exception e) {
                        RC.Error(Error.from(e).whileAttempting("To execute /party leave"));
                    }
                })
        );

        manager.command(manager.commandBuilder("party")
                .literal("invite")
                .optional("player", StringParser.stringParser(), SuggestionProvider.blockingStrings((ctx, in) -> {
                    return RC.P.Players().onlinePlayers().stream()
                            .map(Player::username)
                            .collect(Collectors.toList());
                }))
                .handler(context -> {
                    try {
                        CommandClient client = context.sender();
                        if (!(client instanceof CommandClient.Player playerClient)) return;

                        Player player = RC.P.Players().fetchByID(playerClient.id()).orElse(null);
                        if (player == null) return;

                        String targetName = context.<String>optional("player").orElse(null);
                        if(targetName == null) {
                            player.message(RC.Lang().lang("rcm-party-usage-invite").generate());
                            return;
                        }

                        Player target = RC.P.Players().fetchByUsername(targetName).orElse(null);
                        if(target == null) {
                            player.message(Component.text("Player not found."));
                            return;
                        }

                        PartyRegistry registry = RC.Module("PartyRegistry");
                        Party party = registry.fetch(UUID.fromString(player.id())).orElse(null);

                        if (party == null) {
                            player.message(RC.Lang().lang("rcm-party-not-in-party").generate());
                            return;
                        }

                        if (registry.config().partyLeader.onlyLeaderCanInvite && !party.leader().equals(UUID.fromString(player.id()))) {
                            player.message(RC.Lang().lang("rcm-party-only-leader-invite").generate());
                            return;
                        }

                        party.invite(UUID.fromString(player.id()), UUID.fromString(target.id()));
                        player.message(RC.Lang().lang("rcm-party-invited").generate(target.username()));
                        target.message(RC.Lang().lang("rcm-party-received-invite").generate(player.username()));
                    } catch (Exception e) {
                        RC.Error(Error.from(e).whileAttempting("To execute /party invite"));
                    }
                })
        );

        manager.command(manager.commandBuilder("party")
                .literal("autofollow")
                .handler(context -> {
                    try {
                        CommandClient client = context.sender();
                        if (!(client instanceof CommandClient.Player playerClient)) return;

                        Player player = RC.P.Players().fetchByID(playerClient.id()).orElse(null);
                        if (player == null) return;

                        PartyRegistry registry = RC.Module("PartyRegistry");
                        Party party = registry.fetch(UUID.fromString(player.id())).orElse(null);

                        if (party == null) {
                            player.message(RC.Lang().lang("rcm-party-not-in-party").generate());
                            return;
                        }

                        if (!party.leader().equals(UUID.fromString(player.id()))) {
                            player.message(Component.text("Only the party leader can toggle auto-follow."));
                            return;
                        }

                        boolean newState = !party.autoFollow();
                        party.autoFollow(newState);
                        
                        Component msg = RC.Lang().lang("rcm-party-auto-follow-toggled").generate(newState);
                        party.players().forEach(u -> {
                            RC.P.PlayerFromID(u.toString()).ifPresent(p -> p.message(msg));
                        });
                    } catch (Exception e) {
                        RC.Error(Error.from(e).whileAttempting("To execute /party autofollow"));
                    }
                })
        );
        
        manager.command(manager.commandBuilder("party")
                .literal("send")
                .handler(context -> {
                    try {
                        CommandClient client = context.sender();
                        if (!(client instanceof CommandClient.Player playerClient)) return;

                        Player player = RC.P.Players().fetchByID(playerClient.id()).orElse(null);
                        if (player == null) return;

                        PartyRegistry registry = RC.Module("PartyRegistry");
                        Party party = registry.fetch(UUID.fromString(player.id())).orElse(null);

                        if (party == null) {
                            player.message(RC.Lang().lang("rcm-party-not-in-party").generate());
                            return;
                        }

                        if (!party.leader().equals(UUID.fromString(player.id()))) {
                            player.message(Component.text("Only the party leader can send the party."));
                            return;
                        }

                        Component msg = RC.Lang().lang("rcm-party-teleporting").generate();
                        party.players().forEach(u -> {
                            if (!u.equals(party.leader())) {
                                RC.P.PlayerFromID(u.toString()).ifPresent(p -> {
                                    p.message(msg);
                                    try {
                                        RC.P.Adapter().connectServer(player.server().orElseThrow(), p);
                                    } catch(Exception ignore) {}
                                });
                            }
                        });
                    } catch (Exception e) {
                        RC.Error(Error.from(e).whileAttempting("To execute /party send"));
                    }
                })
        );
        
        manager.command(manager.commandBuilder("party")
                .literal("accept")
                .handler(context -> {
                    try {
                        CommandClient client = context.sender();
                        if (!(client instanceof CommandClient.Player playerClient)) return;

                        Player player = RC.P.Players().fetchByID(playerClient.id()).orElse(null);
                        if (player == null) return;

                        PartyRegistry registry = RC.Module("PartyRegistry");
                        Party.Invitation invitation = registry.fetchInvitation(UUID.fromString(player.id())).orElse(null);

                        if(invitation == null || invitation.expired()) {
                            player.message(Component.text("You do not have any pending party invitations."));
                            return;
                        }
                        
                        Party.JoinAttempt attempt = invitation.accept();
                        player.message(Component.text(attempt.message()));
                    } catch (Exception e) {
                        RC.Error(Error.from(e).whileAttempting("To execute /party accept"));
                    }
                })
        );
    }
}
