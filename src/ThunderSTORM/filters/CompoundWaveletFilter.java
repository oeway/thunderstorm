package ThunderSTORM.filters;

import ThunderSTORM.IModule;
import static ThunderSTORM.utils.ImageProcessor.subtractImage;
import ij.IJ;
import ij.ImagePlus;
import ij.process.FloatProcessor;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

public final class CompoundWaveletFilter implements IFilter, IModule {

    private boolean third_plane;
    
    private WaveletFilter w1, w2, w3;
    
    private JCheckBox thirdCheckBox;
    
    public CompoundWaveletFilter(boolean third_plane) {
        this.third_plane = third_plane;
        //
        w1 = new WaveletFilter(1);
        w2 = new WaveletFilter(2);
        w3 = new WaveletFilter(3);
    }
    
    @Override
    public FloatProcessor filterImage(FloatProcessor image) {
        ImagePlus imp = new ImagePlus();
        //
        FloatProcessor V1 = w1.filterImage(image);
        //
        imp.setProcessor(V1.convertToShort(false)); IJ.save(imp, "../output_V1.tif");
        //
        FloatProcessor V2 = w2.filterImage(V1);
        //
        imp.setProcessor(V2.convertToShort(false)); IJ.save(imp, "../output_V2.tif");
        //
        if (third_plane) {
            FloatProcessor V3 = w3.filterImage(V2);
            return subtractImage(V2, V3);
        }
        //
        imp.setProcessor(subtractImage(V1, V2).convertToShort(false)); IJ.save(imp, "../output_V1-V2.tif");
        //
        return subtractImage(V1, V2);
    }

    @Override
    public String getName() {
        return "Wavelet filter";
    }

    @Override
    public JPanel getOptionsPanel() {
        thirdCheckBox = new JCheckBox("third plane");
        thirdCheckBox.setSelected(third_plane);
        //
        JPanel panel = new JPanel();
        panel.add(thirdCheckBox);
        return panel;
    }

    @Override
    public void readParameters() {
        try {
            third_plane = thirdCheckBox.isSelected();
        } catch(NumberFormatException ex) {
            IJ.showMessage("Error!", ex.getMessage());
        }
    }
    
}
