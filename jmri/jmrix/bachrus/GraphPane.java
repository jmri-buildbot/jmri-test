// GraphPane.java

package jmri.jmrix.bachrus;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.print.*;
import javax.swing.*;

/**
 * Frame for graph of loco speed curves
 *
 * @author			Andrew Crosland   Copyright (C) 2010
 * @version			$Revision: 1.7 $
 */
public class GraphPane extends JPanel implements Printable {
    final int PAD = 40;

    protected String xLabel;
    protected String yLabel;
    // array to hold the speed curves
    protected dccSpeedProfile [] _sp;
    protected String annotate;
    protected Color [] colors = { Color.RED, Color.BLUE };

    // Use a default 28 step profile
    public GraphPane() {
        super();
        _sp = new dccSpeedProfile[1];
        _sp[0] = new dccSpeedProfile(28);
    }

    public GraphPane(dccSpeedProfile sp) {
        super();
        _sp = new dccSpeedProfile[1];
        _sp[0] = sp;
    }

    public GraphPane(dccSpeedProfile sp0, dccSpeedProfile sp1) {
        super();
        _sp = new dccSpeedProfile[2];
        _sp[0] = sp0;
        _sp[1] = sp1;
    }

    public void setXLabel (String s) { xLabel = s; }
    public void setYLabel (String s) { yLabel = s; }

    int units = Speed.MPH;
    String unitString = "Speed (MPH)";
    void setUnitsMph() { units = Speed.MPH; setYLabel("Speed MPH"); }
    void setUnitsKph() { units = Speed.KPH; setYLabel("Speed KPH"); }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGraph(g);
    }

    protected void drawGraph(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        
        // Draw ordinate.
        g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
        // Draw abcissa.
        g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));
        
        // Draw labels.
        Font font = g2.getFont();
        FontRenderContext frc = g2.getFontRenderContext();
        LineMetrics lm = font.getLineMetrics("0", frc);
        float sh = lm.getAscent() + lm.getDescent();
        // Ordinate label.
        float sy = PAD + ((h - 2*PAD) - yLabel.length()*sh)/2 + lm.getAscent();
        g2.setPaint(Color.green.darker());
        for(int i = 0; i < yLabel.length(); i++) {
            String letter = String.valueOf(yLabel.charAt(i));
            float sw = (float)font.getStringBounds(letter, frc).getWidth();
            float sx = (PAD/2 - sw)/2;
            g2.drawString(letter, sx, sy);
            sy += sh;
        }        
        // Abcissa label.
        sy = h - PAD/2 + (PAD/2 - sh)/2 + lm.getAscent();
        float sw = (float)font.getStringBounds(xLabel, frc).getWidth();
        float sx = (w - sw)/2;
        g2.drawString(xLabel, sx, sy);
        
        // Used to scale values into drawing area
        float scale = (h - 2*PAD)/_sp[0].getMax();
        // space between values along the ordinate
        // start with an increment of 10
        int valInc = 10;
        float yInc = scale*valInc;
        if (units == Speed.MPH) {
            // need inverse transform here
            yInc = Speed.mphToKph(yInc);
        }
        if (_sp[0].getMax() > 100) {
            valInc = 20;
            yInc *=2;
        }
        String ordString;
        // Draw lines
        for(int i = 0; i <= (h - 2*PAD)/yInc; i++) {
            float y1 = h - PAD - i*yInc;
            g2.draw(new Line2D.Double(7*PAD/8, y1, PAD, y1));
            ordString = Integer.toString(i*valInc);
            sw = (float)font.getStringBounds(ordString, frc).getWidth();
            sx = 7*PAD/8 - sw;
            sy = y1 + lm.getAscent()/2;
            g2.drawString(ordString, sx, sy);
        }
        
        // The space between values along the abcissa.
        float xInc = (float)(w - 2*PAD)/(_sp[0].getLength()-1);
        String abString;
        // Draw lines between data points.
        g2.setPaint(Color.green.darker());
        for(int i = 0; i < _sp[0].getLength(); i++) {
            float x1 = 0.0F;
            for (int j = 0; j < _sp.length; j++) {
                x1 = PAD + i*xInc;
                float y1 = h - PAD - scale*_sp[j].getPoint(i);
                float x2 = PAD + (i+1)*xInc;
                float y2 = h - PAD - scale*_sp[j].getPoint(i+1);
                if (i <= _sp[j].getLast()-1) {
                    g2.draw(new Line2D.Double(x1, y1, x2, y2));
                }
            }
            // tick marks along abcissa
            g2.draw(new Line2D.Double(x1, h-7*PAD/8, x1, h-PAD));
            if (((i%5) == 0) || (i == _sp[0].getLength()-1)) {
                // abcissa labels every 5 ticks
                abString = Integer.toString(i);
                sw = (float)font.getStringBounds(abString, frc).getWidth();
                sx = x1 - sw/2;
                sy = h - PAD + (PAD/2 - sh)/2 + lm.getAscent();
                g2.drawString(abString, sx, sy);
            }
        }
        
        // Mark data points.
        for(int i = 0; i <= _sp[0].getLast(); i++) {
            for (int j = 0; j < _sp.length; j++) {
                g2.setPaint(colors[j]);
                float x = PAD + i*xInc;
                float y = h - PAD - scale*_sp[j].getPoint(i);
                if (i <= _sp[j].getLast()) {
                    g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
                }
            }
        }
    }

    public int print(Graphics g, PageFormat pf, int page) throws
                                                        PrinterException {

        if (page > 0) { /* We have only one page, and 'page' is zero-based */
            return Printable.NO_SUCH_PAGE;
        }

        Graphics2D g2 = (Graphics2D)g;
        /* User (0,0) is typically outside the imageable area, so we must
         * translate by the X and Y values in the PageFormat to avoid clipping.
         */
        g2.translate(pf.getImageableX(), pf.getImageableY());

        // Scale to fit the width and height if neccessary
        double scale = 1.0;
        if (this.getWidth() > pf.getImageableWidth()) {
            scale *= pf.getImageableWidth()/this.getWidth();
        }
        if (this.getHeight() > pf.getImageableHeight()) {
            scale *= pf.getImageableHeight()/this.getHeight();
        }
        g2.scale(scale, scale);

        // Draw the graph
        drawGraph(g);

        // Add annotation
        g2.setPaint(Color.BLACK);
        g2.drawString(annotate, 0, Math.round(this.getHeight()+2*PAD*scale));

        /* tell the caller that this page is part of the printed document */
        return Printable.PAGE_EXISTS;
    }

    public void printProfile(String s) {
        annotate = s;
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        boolean ok = job.printDialog();
        if (ok) {
            try {
                job.print();
            } catch (PrinterException ex) {
                log.error("Exception whilst printing profile " + ex);
            }
        }
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GraphPane.class.getName());
}