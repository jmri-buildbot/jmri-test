// JoalAudioListener.java

package jmri.jmrit.audio;

import javax.vecmath.Vector3f;
import net.java.games.joal.AL;

/**
 * JOAL implementation of the Audio Listener sub-class.
 * <P>
 * For now, no system-specific implementations are forseen - this will remain
 * internal-only
 * <br><br><hr><br><b>
 *    This software is based on or using the JOAL Library available from
 *    <a href="http://joal.dev.java.net/">http://joal.dev.java.net/</a>
 * </b><br><br>
 *    JOAL License:
 * <br><i>
 * Copyright (c) 2003 Sun Microsystems, Inc. All  Rights Reserved.
 * <br>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <br>
 * -Redistribution of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * <br>
 * -Redistribution in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <br>
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * <br>
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN") AND ITS
 * LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A
 * RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 * IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT
 * OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * <br>
 * You acknowledge that this software is not designed or intended for use in the
 * design, construction, operation or maintenance of any nuclear facility.
 * <br><br><br></i>
 * <hr>
 * This file is part of JMRI.
 * <P>
 * JMRI is free software; you can redistribute it and/or modify it under
 * the terms of version 2 of the GNU General Public License as published
 * by the Free Software Foundation. See the "COPYING" file for a copy
 * of this license.
 * <P>
 * JMRI is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * for more details.
 * <P>
 *
 * @author Matthew Harris  copyright (c) 2009
 * @version $Revision$
 */
public class JoalAudioListener extends AbstractAudioListener {

    private static AL al = JoalAudioFactory.getAL();

    private boolean _initialised = false;

    /**
     * Constructor for new JoalAudioListener with system name
     *
     * @param systemName AudioListener object system name (e.g. IAL)
     */
    public JoalAudioListener(String systemName) {
        super(systemName);
        if (log.isDebugEnabled()) log.debug("New JoalAudioListener: "+systemName);
        _initialised = init();
    }

    /**
     * Constructor for new JoalAudioListener with system name and user name
     *
     * @param systemName AudioListener object system name (e.g. IAL)
     * @param userName AudioListener object user name
     */
    public JoalAudioListener(String systemName, String userName) {
        super(systemName, userName);
        if (log.isDebugEnabled()) log.debug("New JoalAudioListener: "+userName+" ("+systemName+")");
        _initialised = init();
    }

    private boolean init() {
        // Nothing to do for the listener
        return true;
    }

    protected void changePosition(Vector3f pos) {
        if (_initialised) {
            al.alListener3f(AL.AL_POSITION, pos.x, pos.y, pos.z);
            if (JoalAudioFactory.checkALError()) {
                log.warn("Error updating position of JoalAudioListener (" + this.getSystemName() + ")");
            }
        }
    }

    @Override
    public void setVelocity(Vector3f vel) {
        super.setVelocity(vel);
        if (_initialised) {
            al.alListener3f(AL.AL_VELOCITY, vel.x, vel.y, vel.z);
            if (JoalAudioFactory.checkALError()) {
                log.warn("Error updating velocity setting of JoalAudioListener (" + this.getSystemName() + ")");
            }
        }
    }

    @Override
    public void setOrientation(Vector3f at, Vector3f up) {
        super.setOrientation(at, up);
        if (_initialised) {
            al.alListenerfv(AL.AL_ORIENTATION,
                    new float[] { at.x, at.y, at.z,
                                  up.x, up.y, up.z},
                    0);
            if (JoalAudioFactory.checkALError()) {
                log.warn("Error updating orientation of JoalAudioListener (" + this.getSystemName() + ")");
            }
        }
    }

    @Override
    public void setGain(float gain) {
        super.setGain(gain);
        if (_initialised) {
            al.alListenerf(AL.AL_GAIN, gain);
            if (JoalAudioFactory.checkALError()) {
                log.warn("Error updating gain setting of JoalAudioListener (" + this.getSystemName() + ")");
            }
        }
    }

    @Override
    public void setMetersPerUnit(float metersPerUnit) {
        super.setMetersPerUnit(metersPerUnit);
        if (_initialised) {
            al.alListenerf(AL.AL_METERS_PER_UNIT, metersPerUnit);
            if (JoalAudioFactory.checkALError()) {
                log.warn("Error updating meters per unit setting of JoalAudioListener (" + this.getSystemName() + ")");
            }
        }
    }

    @Override
    public void stateChanged(int oldState) {
        super.stateChanged(oldState);
        if (_initialised){
            al.alListenerf(AL.AL_GAIN, this.getGain());
            al.alListener3f(AL.AL_POSITION, this.getCurrentPosition().x, this.getCurrentPosition().y, this.getCurrentPosition().z);
            al.alListener3f(AL.AL_VELOCITY, this.getVelocity().x, this.getVelocity().y, this.getVelocity().z);
            al.alListenerfv(AL.AL_ORIENTATION,
                    new float[] { this.getOrientation(AT).x, this.getOrientation(AT).y, this.getOrientation(AT).z,
                                  this.getOrientation(UP).x, this.getOrientation(UP).y, this.getOrientation(UP).z},
                    0);
            if (JoalAudioFactory.checkALError()) {
                log.warn("Error updating JoalAudioListener (" + this.getSystemName() + ")");
            }
        }
        else {
            _initialised = init();
        }
    }

    protected void cleanUp() {
        // no clean-up needed for Listener
        if (log.isDebugEnabled()) log.debug("Cleanup JoalAudioListener (" + this.getSystemName() + ")");
        this.dispose();
    }

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(JoalAudioListener.class.getName());

}

/* $(#)JoalAudioListener.java */