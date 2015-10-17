package com.sonicjumper.enhancedvisuals.shaders.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class PNGDecoder
{
  public static enum Format
  {
    ALPHA(1, true),  LUMINANCE(1, false),  LUMINANCE_ALPHA(2, true),  RGB(3, false),  RGBA(4, true),  BGRA(4, true),  ABGR(4, true);
    
    final int numComponents;
    final boolean hasAlpha;
    
    private Format(int numComponents, boolean hasAlpha)
    {
      this.numComponents = numComponents;
      this.hasAlpha = hasAlpha;
    }
    
    public int getNumComponents()
    {
      return this.numComponents;
    }
    
    public boolean isHasAlpha()
    {
      return this.hasAlpha;
    }
  }
  
  private static final byte[] SIGNATURE = { -119, 80, 78, 71, 13, 10, 26, 10 };
  private static final int IHDR = 1229472850;
  private static final int PLTE = 1347179589;
  private static final int tRNS = 1951551059;
  private static final int IDAT = 1229209940;
  private static final int IEND = 1229278788;
  private static final byte COLOR_GREYSCALE = 0;
  private static final byte COLOR_TRUECOLOR = 2;
  private static final byte COLOR_INDEXED = 3;
  private static final byte COLOR_GREYALPHA = 4;
  private static final byte COLOR_TRUEALPHA = 6;
  private final InputStream input;
  private final CRC32 crc;
  private final byte[] buffer;
  private int chunkLength;
  private int chunkType;
  private int chunkRemaining;
  private int width;
  private int height;
  private int bitdepth;
  private int colorType;
  private int bytesPerPixel;
  private byte[] palette;
  private byte[] paletteA;
  private byte[] transPixel;
  
  public PNGDecoder(InputStream input)
    throws IOException
  {
    this.input = input;
    this.crc = new CRC32();
    this.buffer = new byte['?'];
    
    readFully(this.buffer, 0, SIGNATURE.length);
    if (!checkSignature(this.buffer)) {
      throw new IOException("Not a valid PNG file");
    }
    openChunk(1229472850);
    readIHDR();
    closeChunk();
    for (;;)
    {
      openChunk();
      switch (this.chunkType)
      {
      case 1229209940: 
        break;
      case 1347179589: 
        readPLTE();
        break;
      case 1951551059: 
        readtRNS();
      }
      closeChunk();
    }
    /*if ((this.colorType == 3) && (this.palette == null)) {
      throw new IOException("Missing PLTE chunk");
    }*/
  }
  
  public int getHeight()
  {
    return this.height;
  }
  
  public int getWidth()
  {
    return this.width;
  }
  
  public boolean hasAlphaChannel()
  {
    return (this.colorType == 6) || (this.colorType == 4);
  }
  
  public boolean hasAlpha()
  {
    return (hasAlphaChannel()) || (this.paletteA != null) || (this.transPixel != null);
  }
  
  public boolean isRGB()
  {
    return (this.colorType == 6) || (this.colorType == 2) || (this.colorType == 3);
  }
  
  public void overwriteTRNS(byte r, byte g, byte b)
  {
    if (hasAlphaChannel()) {
      throw new UnsupportedOperationException("image has an alpha channel");
    }
    byte[] pal = this.palette;
    if (pal == null)
    {
      this.transPixel = new byte[] { 0, r, 0, g, 0, b };
    }
    else
    {
      this.paletteA = new byte[pal.length / 3];
      int i = 0;
      for (int j = 0; i < pal.length; j++)
      {
        if ((pal[i] != r) || (pal[(i + 1)] != g) || (pal[(i + 2)] != b)) {
          this.paletteA[j] = -1;
        }
        i += 3;
      }
    }
  }
  
  public Format decideTextureFormat(Format fmt)
  {
    switch (this.colorType)
    {
    case 2: 
      switch (fmt)
      {
      case ABGR: 
      case RGBA: 
      case BGRA: 
      case RGB: 
        return fmt;
      default:
		break;
      }
      return Format.RGB;
    case 6: 
      switch (fmt)
      {
      case ABGR: 
      case RGBA: 
      case BGRA: 
      case RGB: 
        return fmt;
      default:
  		break;
      }
      return Format.RGBA;
    case 0: 
      switch (fmt)
      {
      case LUMINANCE: 
      case ALPHA: 
        return fmt;
      default:
  		break;
      }
      return Format.LUMINANCE;
    case 4: 
      return Format.LUMINANCE_ALPHA;
    case 3: 
      switch (fmt)
      {
      case ABGR: 
      case RGBA: 
      case BGRA: 
        return fmt;
      default:
  		break;
      }
      return Format.RGBA;
    }
    throw new UnsupportedOperationException("Not yet implemented");
  }
  
  public void decode(ByteBuffer buffer, int stride, Format fmt)
    throws IOException
  {
    int offset = buffer.position();
    int lineSize = (this.width * this.bitdepth + 7) / 8 * this.bytesPerPixel;
    byte[] curLine = new byte[lineSize + 1];
    byte[] prevLine = new byte[lineSize + 1];
    byte[] palLine = this.bitdepth < 8 ? new byte[this.width + 1] : null;
    
    Inflater inflater = new Inflater();
    try
    {
      for (int y = 0; y < this.height; y++)
      {
        readChunkUnzip(inflater, curLine, 0, curLine.length);
        unfilter(curLine, prevLine);
        
        buffer.position(offset + y * stride);
        switch (this.colorType)
        {
        case 2: 
          switch (fmt)
          {
          case ABGR: 
            copyRGBtoABGR(buffer, curLine); break;
          case RGBA: 
            copyRGBtoRGBA(buffer, curLine); break;
          case BGRA: 
            copyRGBtoBGRA(buffer, curLine); break;
          case RGB: 
            copy(buffer, curLine); break;
          default: 
            throw new UnsupportedOperationException("Unsupported format for this image");
          }
          break;
        case 6: 
          switch (fmt)
          {
          case ABGR: 
            copyRGBAtoABGR(buffer, curLine); break;
          case RGBA: 
            copy(buffer, curLine); break;
          case BGRA: 
            copyRGBAtoBGRA(buffer, curLine); break;
          case RGB: 
            copyRGBAtoRGB(buffer, curLine); break;
          default: 
            throw new UnsupportedOperationException("Unsupported format for this image");
          }
          break;
        case 0: 
          switch (fmt)
          {
          case LUMINANCE: 
          case ALPHA: 
            copy(buffer, curLine); break;
          default: 
            throw new UnsupportedOperationException("Unsupported format for this image");
          }
          break;
        case 4: 
          switch (fmt)
          {
          case LUMINANCE_ALPHA: 
            copy(buffer, curLine); break;
          default: 
            throw new UnsupportedOperationException("Unsupported format for this image");
          }
          break;
        case 3: 
          switch (this.bitdepth)
          {
          case 8: 
            palLine = curLine; break;
          case 4: 
            expand4(curLine, palLine); break;
          case 2: 
            expand2(curLine, palLine); break;
          case 1: 
            expand1(curLine, palLine); break;
          case 3: 
          case 5: 
          case 6: 
          case 7: 
          default: 
            throw new UnsupportedOperationException("Unsupported bitdepth for this image");
          }
          switch (fmt)
          {
          case ABGR: 
            copyPALtoABGR(buffer, palLine); break;
          case RGBA: 
            copyPALtoRGBA(buffer, palLine); break;
          case BGRA: 
            copyPALtoBGRA(buffer, palLine); break;
          default: 
            throw new UnsupportedOperationException("Unsupported format for this image");
          }
          break;
        case 1: 
        case 5: 
        default: 
          throw new UnsupportedOperationException("Not yet implemented");
        }
        byte[] tmp = curLine;
        curLine = prevLine;
        prevLine = tmp;
      }
    }
    finally
    {
      inflater.end();
    }
  }
  
  public void decodeFlipped(ByteBuffer buffer, int stride, Format fmt)
    throws IOException
  {
    if (stride <= 0) {
      throw new IllegalArgumentException("stride");
    }
    int pos = buffer.position();
    int posDelta = (this.height - 1) * stride;
    buffer.position(pos + posDelta);
    decode(buffer, -stride, fmt);
    buffer.position(buffer.position() + posDelta);
  }
  
  private void copy(ByteBuffer buffer, byte[] curLine)
  {
    buffer.put(curLine, 1, curLine.length - 1);
  }
  
  private void copyRGBtoABGR(ByteBuffer buffer, byte[] curLine)
  {
    if (this.transPixel != null)
    {
      byte tr = this.transPixel[1];
      byte tg = this.transPixel[3];
      byte tb = this.transPixel[5];
      int i = 1;
      for (int n = curLine.length; i < n; i += 3)
      {
        byte r = curLine[i];
        byte g = curLine[(i + 1)];
        byte b = curLine[(i + 2)];
        byte a = -1;
        if ((r == tr) && (g == tg) && (b == tb)) {
          a = 0;
        }
        buffer.put(a).put(b).put(g).put(r);
      }
    }
    else
    {
      int i = 1;
      for (int n = curLine.length; i < n; i += 3) {
        buffer.put((byte)-1).put(curLine[(i + 2)]).put(curLine[(i + 1)]).put(curLine[i]);
      }
    }
  }
  
  private void copyRGBtoRGBA(ByteBuffer buffer, byte[] curLine)
  {
    if (this.transPixel != null)
    {
      byte tr = this.transPixel[1];
      byte tg = this.transPixel[3];
      byte tb = this.transPixel[5];
      int i = 1;
      for (int n = curLine.length; i < n; i += 3)
      {
        byte r = curLine[i];
        byte g = curLine[(i + 1)];
        byte b = curLine[(i + 2)];
        byte a = -1;
        if ((r == tr) && (g == tg) && (b == tb)) {
          a = 0;
        }
        buffer.put(r).put(g).put(b).put(a);
      }
    }
    else
    {
      int i = 1;
      for (int n = curLine.length; i < n; i += 3) {
        buffer.put(curLine[i]).put(curLine[(i + 1)]).put(curLine[(i + 2)]).put((byte)-1);
      }
    }
  }
  
  private void copyRGBtoBGRA(ByteBuffer buffer, byte[] curLine)
  {
    if (this.transPixel != null)
    {
      byte tr = this.transPixel[1];
      byte tg = this.transPixel[3];
      byte tb = this.transPixel[5];
      int i = 1;
      for (int n = curLine.length; i < n; i += 3)
      {
        byte r = curLine[i];
        byte g = curLine[(i + 1)];
        byte b = curLine[(i + 2)];
        byte a = -1;
        if ((r == tr) && (g == tg) && (b == tb)) {
          a = 0;
        }
        buffer.put(b).put(g).put(r).put(a);
      }
    }
    else
    {
      int i = 1;
      for (int n = curLine.length; i < n; i += 3) {
        buffer.put(curLine[(i + 2)]).put(curLine[(i + 1)]).put(curLine[i]).put((byte)-1);
      }
    }
  }
  
  private void copyRGBAtoABGR(ByteBuffer buffer, byte[] curLine)
  {
    int i = 1;
    for (int n = curLine.length; i < n; i += 4) {
      buffer.put(curLine[(i + 3)]).put(curLine[(i + 2)]).put(curLine[(i + 1)]).put(curLine[i]);
    }
  }
  
  private void copyRGBAtoBGRA(ByteBuffer buffer, byte[] curLine)
  {
    int i = 1;
    for (int n = curLine.length; i < n; i += 4) {
      buffer.put(curLine[(i + 2)]).put(curLine[(i + 1)]).put(curLine[i]).put(curLine[(i + 3)]);
    }
  }
  
  private void copyRGBAtoRGB(ByteBuffer buffer, byte[] curLine)
  {
    int i = 1;
    for (int n = curLine.length; i < n; i += 4) {
      buffer.put(curLine[i]).put(curLine[(i + 1)]).put(curLine[(i + 2)]);
    }
  }
  
  private void copyPALtoABGR(ByteBuffer buffer, byte[] curLine)
  {
    if (this.paletteA != null)
    {
      int i = 1;
      for (int n = curLine.length; i < n; i++)
      {
        int idx = curLine[i] & 0xFF;
        byte r = this.palette[(idx * 3 + 0)];
        byte g = this.palette[(idx * 3 + 1)];
        byte b = this.palette[(idx * 3 + 2)];
        byte a = this.paletteA[idx];
        buffer.put(a).put(b).put(g).put(r);
      }
    }
    else
    {
      int i = 1;
      for (int n = curLine.length; i < n; i++)
      {
        int idx = curLine[i] & 0xFF;
        byte r = this.palette[(idx * 3 + 0)];
        byte g = this.palette[(idx * 3 + 1)];
        byte b = this.palette[(idx * 3 + 2)];
        byte a = -1;
        buffer.put(a).put(b).put(g).put(r);
      }
    }
  }
  
  private void copyPALtoRGBA(ByteBuffer buffer, byte[] curLine)
  {
    if (this.paletteA != null)
    {
      int i = 1;
      for (int n = curLine.length; i < n; i++)
      {
        int idx = curLine[i] & 0xFF;
        byte r = this.palette[(idx * 3 + 0)];
        byte g = this.palette[(idx * 3 + 1)];
        byte b = this.palette[(idx * 3 + 2)];
        byte a = this.paletteA[idx];
        buffer.put(r).put(g).put(b).put(a);
      }
    }
    else
    {
      int i = 1;
      for (int n = curLine.length; i < n; i++)
      {
        int idx = curLine[i] & 0xFF;
        byte r = this.palette[(idx * 3 + 0)];
        byte g = this.palette[(idx * 3 + 1)];
        byte b = this.palette[(idx * 3 + 2)];
        byte a = -1;
        buffer.put(r).put(g).put(b).put(a);
      }
    }
  }
  
  private void copyPALtoBGRA(ByteBuffer buffer, byte[] curLine)
  {
    if (this.paletteA != null)
    {
      int i = 1;
      for (int n = curLine.length; i < n; i++)
      {
        int idx = curLine[i] & 0xFF;
        byte r = this.palette[(idx * 3 + 0)];
        byte g = this.palette[(idx * 3 + 1)];
        byte b = this.palette[(idx * 3 + 2)];
        byte a = this.paletteA[idx];
        buffer.put(b).put(g).put(r).put(a);
      }
    }
    else
    {
      int i = 1;
      for (int n = curLine.length; i < n; i++)
      {
        int idx = curLine[i] & 0xFF;
        byte r = this.palette[(idx * 3 + 0)];
        byte g = this.palette[(idx * 3 + 1)];
        byte b = this.palette[(idx * 3 + 2)];
        byte a = -1;
        buffer.put(b).put(g).put(r).put(a);
      }
    }
  }
  
  private void expand4(byte[] src, byte[] dst)
  {
    int i = 1;
    for (int n = dst.length; i < n; i += 2)
    {
      int val = src[(1 + (i >> 1))] & 0xFF;
      switch (n - i)
      {
      default: 
        dst[(i + 1)] = ((byte)(val & 0xF));
      }
      dst[i] = ((byte)(val >> 4));
    }
  }
  
  private void expand2(byte[] src, byte[] dst)
  {
    int i = 1;
    for (int n = dst.length; i < n; i += 4)
    {
      int val = src[(1 + (i >> 2))] & 0xFF;
      switch (n - i)
      {
      default: 
        dst[(i + 3)] = ((byte)(val & 0x3));
      case 3: 
        dst[(i + 2)] = ((byte)(val >> 2 & 0x3));
      case 2: 
        dst[(i + 1)] = ((byte)(val >> 4 & 0x3));
      }
      dst[i] = ((byte)(val >> 6));
    }
  }
  
  private void expand1(byte[] src, byte[] dst)
  {
    int i = 1;
    for (int n = dst.length; i < n; i += 8)
    {
      int val = src[(1 + (i >> 3))] & 0xFF;
      switch (n - i)
      {
      default: 
        dst[(i + 7)] = ((byte)(val & 0x1));
      case 7: 
        dst[(i + 6)] = ((byte)(val >> 1 & 0x1));
      case 6: 
        dst[(i + 5)] = ((byte)(val >> 2 & 0x1));
      case 5: 
        dst[(i + 4)] = ((byte)(val >> 3 & 0x1));
      case 4: 
        dst[(i + 3)] = ((byte)(val >> 4 & 0x1));
      case 3: 
        dst[(i + 2)] = ((byte)(val >> 5 & 0x1));
      case 2: 
        dst[(i + 1)] = ((byte)(val >> 6 & 0x1));
      }
      dst[i] = ((byte)(val >> 7));
    }
  }
  
  private void unfilter(byte[] curLine, byte[] prevLine)
    throws IOException
  {
    switch (curLine[0])
    {
    case 0: 
      break;
    case 1: 
      unfilterSub(curLine);
      break;
    case 2: 
      unfilterUp(curLine, prevLine);
      break;
    case 3: 
      unfilterAverage(curLine, prevLine);
      break;
    case 4: 
      unfilterPaeth(curLine, prevLine);
      break;
    default: 
      throw new IOException("invalide filter type in scanline: " + curLine[0]);
    }
  }
  
  private void unfilterSub(byte[] curLine)
  {
    int bpp = this.bytesPerPixel;
    int i = bpp + 1;
    for (int n = curLine.length; i < n; i++)
    {
      int tmp21_20 = i; byte[] tmp21_19 = curLine;tmp21_19[tmp21_20] = ((byte)(tmp21_19[tmp21_20] + curLine[(i - bpp)]));
    }
  }
  
  private void unfilterUp(byte[] curLine, byte[] prevLine)
  {
    int bpp = this.bytesPerPixel;
    int i = 1;
    for (int n = curLine.length; i < n; i++)
    {
      int tmp22_20 = i; byte[] tmp22_19 = curLine;tmp22_19[tmp22_20] = ((byte)(tmp22_19[tmp22_20] + prevLine[i]));
    }
  }
  
  private void unfilterAverage(byte[] curLine, byte[] prevLine)
  {
    int bpp = this.bytesPerPixel;
    int i;
    for (i = 1; i <= bpp; i++)
    {
      int tmp17_15 = i; byte[] tmp17_14 = curLine;tmp17_14[tmp17_15] = ((byte)(tmp17_14[tmp17_15] + (byte)((prevLine[i] & 0xFF) >>> 1)));
    }
    for (int n = curLine.length; i < n; i++)
    {
      int tmp53_51 = i; byte[] tmp53_50 = curLine;tmp53_50[tmp53_51] = ((byte)(tmp53_50[tmp53_51] + (byte)((prevLine[i] & 0xFF) + (curLine[(i - bpp)] & 0xFF) >>> 1)));
    }
  }
  
  private void unfilterPaeth(byte[] curLine, byte[] prevLine)
  {
    int bpp = this.bytesPerPixel;
    int i;
    for (i = 1; i <= bpp; i++)
    {
      int tmp17_15 = i; byte[] tmp17_14 = curLine;tmp17_14[tmp17_15] = ((byte)(tmp17_14[tmp17_15] + prevLine[i]));
    }
    for (int n = curLine.length; i < n; i++)
    {
      int a = curLine[(i - bpp)] & 0xFF;
      int b = prevLine[i] & 0xFF;
      int c = prevLine[(i - bpp)] & 0xFF;
      int p = a + b - c;
      int pa = p - a;
      if (pa < 0) {
        pa = -pa;
      }
      int pb = p - b;
      if (pb < 0) {
        pb = -pb;
      }
      int pc = p - c;
      if (pc < 0) {
        pc = -pc;
      }
      if ((pa <= pb) && (pa <= pc)) {
        c = a;
      } else if (pb <= pc) {
        c = b;
      }
      int tmp173_171 = i; byte[] tmp173_170 = curLine;tmp173_170[tmp173_171] = ((byte)(tmp173_170[tmp173_171] + (byte)c));
    }
  }
  
  private void readIHDR()
    throws IOException
  {
    checkChunkLength(13);
    readChunk(this.buffer, 0, 13);
    this.width = readInt(this.buffer, 0);
    this.height = readInt(this.buffer, 4);
    this.bitdepth = (this.buffer[8] & 0xFF);
    this.colorType = (this.buffer[9] & 0xFF);
    switch (this.colorType)
    {
    case 0: 
      if (this.bitdepth != 8) {
        throw new IOException("Unsupported bit depth: " + this.bitdepth);
      }
      this.bytesPerPixel = 1;
      break;
    case 4: 
      if (this.bitdepth != 8) {
        throw new IOException("Unsupported bit depth: " + this.bitdepth);
      }
      this.bytesPerPixel = 2;
      break;
    case 2: 
      if (this.bitdepth != 8) {
        throw new IOException("Unsupported bit depth: " + this.bitdepth);
      }
      this.bytesPerPixel = 3;
      break;
    case 6: 
      if (this.bitdepth != 8) {
        throw new IOException("Unsupported bit depth: " + this.bitdepth);
      }
      this.bytesPerPixel = 4;
      break;
    case 3: 
      switch (this.bitdepth)
      {
      case 1: 
      case 2: 
      case 4: 
      case 8: 
        this.bytesPerPixel = 1;
        break;
      case 3: 
      case 5: 
      case 6: 
      case 7: 
      default: 
        throw new IOException("Unsupported bit depth: " + this.bitdepth);
      }
      break;
    case 1: 
    case 5: 
    default: 
      throw new IOException("unsupported color format: " + this.colorType);
    }
    if (this.buffer[10] != 0) {
      throw new IOException("unsupported compression method");
    }
    if (this.buffer[11] != 0) {
      throw new IOException("unsupported filtering method");
    }
    if (this.buffer[12] != 0) {
      throw new IOException("unsupported interlace method");
    }
  }
  
  private void readPLTE()
    throws IOException
  {
    int paletteEntries = this.chunkLength / 3;
    if ((paletteEntries < 1) || (paletteEntries > 256) || (this.chunkLength % 3 != 0)) {
      throw new IOException("PLTE chunk has wrong length");
    }
    this.palette = new byte[paletteEntries * 3];
    readChunk(this.palette, 0, this.palette.length);
  }
  
  private void readtRNS()
    throws IOException
  {
    switch (this.colorType)
    {
    case 0: 
      checkChunkLength(2);
      this.transPixel = new byte[2];
      readChunk(this.transPixel, 0, 2);
      break;
    case 2: 
      checkChunkLength(6);
      this.transPixel = new byte[6];
      readChunk(this.transPixel, 0, 6);
      break;
    case 3: 
      if (this.palette == null) {
        throw new IOException("tRNS chunk without PLTE chunk");
      }
      this.paletteA = new byte[this.palette.length / 3];
      Arrays.fill(this.paletteA, (byte)-1);
      readChunk(this.paletteA, 0, this.paletteA.length);
      break;
    }
  }
  
  private void closeChunk()
    throws IOException
  {
    if (this.chunkRemaining > 0)
    {
      skip(this.chunkRemaining + 4);
    }
    else
    {
      readFully(this.buffer, 0, 4);
      int expectedCrc = readInt(this.buffer, 0);
      int computedCrc = (int)this.crc.getValue();
      if (computedCrc != expectedCrc) {
        throw new IOException("Invalid CRC");
      }
    }
    this.chunkRemaining = 0;
    this.chunkLength = 0;
    this.chunkType = 0;
  }
  
  private void openChunk()
    throws IOException
  {
    readFully(this.buffer, 0, 8);
    this.chunkLength = readInt(this.buffer, 0);
    this.chunkType = readInt(this.buffer, 4);
    this.chunkRemaining = this.chunkLength;
    this.crc.reset();
    this.crc.update(this.buffer, 4, 4);
  }
  
  private void openChunk(int expected)
    throws IOException
  {
    openChunk();
    if (this.chunkType != expected) {
      throw new IOException("Expected chunk: " + Integer.toHexString(expected));
    }
  }
  
  private void checkChunkLength(int expected)
    throws IOException
  {
    if (this.chunkLength != expected) {
      throw new IOException("Chunk has wrong size");
    }
  }
  
  private int readChunk(byte[] buffer, int offset, int length)
    throws IOException
  {
    if (length > this.chunkRemaining) {
      length = this.chunkRemaining;
    }
    readFully(buffer, offset, length);
    this.crc.update(buffer, offset, length);
    this.chunkRemaining -= length;
    return length;
  }
  
  private void refillInflater(Inflater inflater)
    throws IOException
  {
    while (this.chunkRemaining == 0)
    {
      closeChunk();
      openChunk(1229209940);
    }
    int read = readChunk(this.buffer, 0, this.buffer.length);
    inflater.setInput(this.buffer, 0, read);
  }
  
  private void readChunkUnzip(Inflater inflater, byte[] buffer, int offset, int length)
    throws IOException
  {
    assert (buffer != this.buffer);
    try
    {
      do
      {
        int read = inflater.inflate(buffer, offset, length);
        if (read <= 0)
        {
          if (inflater.finished()) {
            throw new EOFException();
          }
          if (inflater.needsInput()) {
            refillInflater(inflater);
          } else {
            throw new IOException("Can't inflate " + length + " bytes");
          }
        }
        else
        {
          offset += read;
          length -= read;
        }
      } while (length > 0);
    }
    catch (DataFormatException ex)
    {
      throw ((IOException)new IOException("inflate error").initCause(ex));
    }
  }
  
  private void readFully(byte[] buffer, int offset, int length)
    throws IOException
  {
    do
    {
      int read = this.input.read(buffer, offset, length);
      if (read < 0) {
        throw new EOFException();
      }
      offset += read;
      length -= read;
    } while (length > 0);
  }
  
  private int readInt(byte[] buffer, int offset)
  {
    return buffer[offset] << 24 | (buffer[(offset + 1)] & 0xFF) << 16 | (buffer[(offset + 2)] & 0xFF) << 8 | buffer[(offset + 3)] & 0xFF;
  }
  
  private void skip(long amount)
    throws IOException
  {
    while (amount > 0L)
    {
      long skipped = this.input.skip(amount);
      if (skipped < 0L) {
        throw new EOFException();
      }
      amount -= skipped;
    }
  }
  
  private static boolean checkSignature(byte[] buffer)
  {
    for (int i = 0; i < SIGNATURE.length; i++) {
      if (buffer[i] != SIGNATURE[i]) {
        return false;
      }
    }
    return true;
  }
}
