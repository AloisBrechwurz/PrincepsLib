package chat;

import biz.princeps.lib.PrincepsLib;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.List;

/**
 * Created by spatium on 18.07.17.
 */
public class MultiPagedMessage {

    private String command;

    private String header;
    private int perSite;
    private List<String> elements;
    private String previous, next;

    private int pointer;

    // General Design idea

    /**
     * this is the header, which can be custom crafted
     * obj1
     * obj2
     * obj3
     * ... perSite
     * <<Previous>   <Next>
     */
    public MultiPagedMessage(String command, String header, int perSite, List<String> elements, String previous, String next, int pointer) {
        this.command = command;
        this.header = header;
        this.perSite = perSite;
        this.elements = elements;
        this.previous = previous;
        this.next = next;
        this.pointer = pointer;
    }

    public BaseComponent[] create() {
        ComponentBuilder builder = new ComponentBuilder(format(header));
        builder.append("\n");

        int siteNumberToDisplay = pointer;

        for (int i = siteNumberToDisplay * perSite; i < (siteNumberToDisplay + 1) * perSite; i++) {
            if (i < elements.size()) {
                builder.append(elements.get(i));
                builder.append("\n");
            }
        }

        pointer = siteNumberToDisplay;
        // System.out.println(siteNumberToDisplay);
        String cmd = command + " ";

        if (siteNumberToDisplay > 0) {
            builder.append(format(previous))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd + --pointer));
        }
        pointer = siteNumberToDisplay;

        if (siteNumberToDisplay < Math.ceil((double) elements.size() / (double) perSite) - 1) {
            builder.append(format(next))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd + ++pointer));
        }
        return builder.create();
    }

    private String format(String header) {
        return ChatColor.translateAlternateColorCodes('&', header);
    }


    public static class MultiPagedMessageBuilder {

        private String command;
        private String header;
        private int perSite;
        private List<String> elements;
        private String previous, next;

        private int pointer;

        public MultiPagedMessageBuilder(int perSite, List<String> elements) {
            this.perSite = perSite;
            this.elements = elements;
        }

        public MultiPagedMessageBuilder() {
        }

        public MultiPagedMessageBuilder setHeaderString(String header) {
            this.header = header;
            return this;
        }

        public MultiPagedMessageBuilder setPerSite(int perSite) {
            this.perSite = perSite;
            return this;
        }

        public MultiPagedMessageBuilder setElements(List<String> elements) {
            this.elements = elements;
            return this;
        }

        public MultiPagedMessageBuilder setPreviousString(String previous) {
            this.previous = previous;
            return this;
        }

        public MultiPagedMessageBuilder setNextString(String next) {
            this.next = next;
            return this;
        }

        public MultiPagedMessageBuilder setCommand(String command, String[] args) {
            this.command = "/" + command;
            if (args.length == 1) {
                try {
                    int i = Integer.parseInt(args[0]);
                    pointer = i;
                } catch (NumberFormatException e) {
                    PrincepsLib.getPluginInstance().getLogger().warning("Argument cannot be parsed to int: " + args[0]);
                }
            }
            return this;
        }

        public MultiPagedMessage build() {
            if (command == null || header == null || perSite < 1 || elements == null || previous == null || next == null)
                throw new NullPointerException("Cant create a MultiPagedMessage with null parameters!");
            return new MultiPagedMessage(command, header, perSite, elements, previous, next, pointer);
        }
    }
}
