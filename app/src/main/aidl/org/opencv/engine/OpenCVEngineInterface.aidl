package org.opencv.engine;

/**
 * Class provides a Java interface for OpenCV Engine Service.
 */
interface OpenCVEngineInterface
{
    /**
     * @return Returns service version.
     */
    int getEngineVersion();

    /**
     * Finds an installed OpenCV library.
     */
    String getLibPathByVersion(String version);

    /**
     * Tries to install defined version of OpenCV from Google Play Market.
     */
    boolean installVersion(String version);
}
