import java.util.Vector;

/**
 * This class represents an optimized Color lookup table.
 */
public class CLUT {
    private Vector<CLUTEntry> entries;

    public CLUT() {
        entries = new Vector<CLUTEntry>();
    }

    /**
     * This method can be used to create an entry in the CLUT.
     *
     * @param codeword     String representing the binary representation of the entry
     * @param range_start  Lower bound of color values represented by the entry
     * @param range_end    Upper bound of color values represented by the entry
     * @param mapped_value Value, which respresents the CLUT entry.
     * @param channel      Color channel is entry is used for
     */
    public void addEntry(String codeword, int range_start, int range_end, int mapped_value, Channel channel) {
        entries.add(new CLUTEntry(codeword, range_start, range_end, mapped_value, channel));
    }

    public void printCLUT() {
        for (int i = 0; i < entries.size(); i++) {
            System.out.print("Channel(" + entries.get(i).channel + "): ");
            System.out.print("[" + entries.get(i).range_start + "," + entries.get(i).range_end + "]");
            System.out.print("\t-->\t" + entries.get(i).mapped_value);
            System.out.print("\tcode=" + entries.get(i).codeword);

            System.out.println();
        }
    }

    int mapValue(int value, Channel channel) {
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).channel.equals(channel)) {
                if (entries.get(i).range_start <= value && entries.get(i).range_end >= value)
                    return entries.get(i).mapped_value;
            }
        }

        System.out.println("Mapping not successful. CLUT incomplete.");
        return 0;
    }

    public class CLUTEntry {

        String codeword;
        int range_start;
        int range_end;
        int mapped_value;
        Channel channel;


        public CLUTEntry(String codeword, int range_start, int range_end, int mapped_value, Channel channel) {
            this.codeword = codeword;
            this.range_start = range_start;
            this.range_end = range_end;
            this.mapped_value = mapped_value;
            this.channel = channel;
        }
    }

    public enum Channel {
        R,
        G,
        B
    }
}

