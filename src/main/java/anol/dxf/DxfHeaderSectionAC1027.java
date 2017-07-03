package anol.dxf;

import java.io.BufferedWriter;
import java.io.IOException;

public class DxfHeaderSectionAC1027 extends DxfHeaderSection{

    public DxfHeaderSectionAC1027(BufferedWriter writer) {
        super(writer);
    }

    public void printSection() throws IOException {
        printVariable(0, "SECTION");
        printVariable(2, "HEADER");
        printVariable(9, "$ACADVER", 1, "AC1027");
        printVariable(9, "$ACADMAINTVER", 70, 105);
        printVariable(9, "$DWGCODEPAGE", 3, "ANSI_1252");
        printVariable(9, "$REQUIREDVERSIONS", 160, 0);
        printVariable(9, "$INSBASE",10, 0.0, 20, 0.0, 30, 0.0);
        printVariable(9, "$EXTMIN",10, 1.000000000000000E+20, 20, 1.000000000000000E+20, 30, 1.000000000000000E+20);
        printVariable(9, "$EXTMAX",10, -1.000000000000000E+20, 20, -1.000000000000000E+20, 30, -1.000000000000000E+20);
        printVariable(9, "$LIMMIN",10, 0.0, 20, 0.0);
        printVariable(9, "$LIMMAX",10, 12.0, 20, 9.0);
        printVariable(9, "$ORTHOMODE", 70, 0);
        printVariable(9, "$REGENMODE", 70, 1);
        printVariable(9, "$FILLMODE", 70, 1);
        printVariable(9, "$QTEXTMODE", 70, 0);
        printVariable(9, "$MIRRTEXT", 70, 1);
        printVariable(9, "$LTSCALE", 40, 1.0);
        printVariable(9, "$ATTMODE", 70, 1);
        printVariable(9, "$TEXTSIZE", 40, 0.2);
        printVariable(9, "$TRACEWID", 40, 0.05);
        printVariable(9, "$TEXTSTYLE", 7, "Standard");
        printVariable(9, "$CLAYER", 8, 0);
        printVariable(9, "$CELTYPE", 6, "ByLayer");
        printVariable(9, "$CECOLOR", 62, 256);
        printVariable(9, "$CELTSCALE", 40, 1.0);
        printVariable(9, "$DISPSILH", 70, 0);
        printVariable(9, "$DIMSCALE", 40, 1.0);
        printVariable(9, "$DIMASZ", 40, 2.5);
        printVariable(9, "$DIMEXO", 40, 0.625);
        printVariable(9, "$DIMDLI", 40, 0.38);
        printVariable(9, "$DIMRND", 40, 0.0);
        printVariable(9, "$DIMDLE", 40, 0.0);
        printVariable(9, "$DIMEXE", 40, 1.25);
        printVariable(9, "$DIMTP", 40, 0.0);
        printVariable(9, "$DIMTM", 40, 0.0);
        printVariable(9, "$DIMTXT", 40, 2.5);
        printVariable(9, "$DIMCEN", 40, 0.09);
        printVariable(9, "$DIMTSZ", 40, 0.0);
        printVariable(9, "$DIMTOL", 70, 0);
        printVariable(9, "$DIMLIM", 70, 0);
        printVariable(9, "$DIMTIH", 70, 1);
        printVariable(9, "$DIMTOH", 70, 1);
        printVariable(9, "$DIMSE1", 70, 0);
        printVariable(9, "$DIMSE2", 70, 0);
        printVariable(9, "$DIMTAD", 70, 0);
        printVariable(9, "$DIMZIN", 70, 8);
        printVariable(9, "$DIMBLK", 1, emptyString);
        printVariable(9, "$DIMASO", 70, 1);
        printVariable(9, "$DIMSHO", 70, 1);
        printVariable(9, "$DIMPOST", 1, emptyString);
        printVariable(9, "$DIMAPOST", 1, emptyString);
        printVariable(9, "$DIMALT", 70, 0);
        printVariable(9, "$DIMALTD", 70, 2);
        printVariable(9, "$DIMALTF", 40, 25.4);
        printVariable(9, "$DIMLFAC", 40, 1.0);
        printVariable(9, "$DIMTOFL", 70, 0);
        printVariable(9, "$DIMTVP", 40, 0.0);
        printVariable(9, "$DIMTIX", 70, 0);
        printVariable(9, "$DIMSOXD", 70, 0);
        printVariable(9, "$DIMSAH", 70, 0);
        printVariable(9, "$DIMBLK1", 1, emptyString);
        printVariable(9, "$DIMBLK2", 1, emptyString);
        printVariable(9, "$DIMSTYLE", 2, "Standard");
        printVariable(9, "$DIMCLRD", 70, 0);
        printVariable(9, "$DIMCLRE", 70, 0);
        printVariable(9, "$DIMCLRT", 70, 0);
        printVariable(9, "$DIMTFAC", 40, 1.0);
        printVariable(9, "$DIMGAP", 40, 0.625);
        printVariable(9, "$DIMJUST", 70, 0);
        printVariable(9, "$DIMSD1", 70, 0);
        printVariable(9, "$DIMSD2", 70, 0);
        printVariable(9, "$DIMTOLJ", 70, 1);
        printVariable(9, "$DIMTZIN", 70, 0);
        printVariable(9, "$DIMALTZ", 70, 0);
        printVariable(9, "$DIMALTTZ", 70, 0);
        printVariable(9, "$DIMUPT", 70, 0);
        printVariable(9, "$DIMDEC", 70, 4);
        printVariable(9, "$DIMTDEC", 70, 4);
        printVariable(9, "$DIMALTU", 70, 2);
        printVariable(9, "$DIMALTTD", 70, 2);
        printVariable(9, "$DIMTXSTY", 7, "Standard");
        printVariable(9, "$DIMAUNIT", 70, 0);
        printVariable(9, "$DIMADEC", 70, 0);
        printVariable(9, "$DIMALTRND", 40, 0.0);
        printVariable(9, "$DIMAZIN", 70, 2);
        printVariable(9, "$DIMDSEP", 70, 46);
        printVariable(9, "$DIMATFIT", 70, 3);
        printVariable(9, "$DIMFRAC", 70, 0);
        printVariable(9, "$DIMLDRBLK", 1, emptyString);
        printVariable(9, "$DIMLUNIT", 70, 2);
        printVariable(9, "$DIMLWD", 70, -2);
        printVariable(9, "$DIMLWE", 70, -2);
        printVariable(9, "$DIMTMOVE", 70, 0);
        printVariable(9, "$DIMFXL", 40, 1.0);
        printVariable(9, "$DIMFXLON", 70, 0);
        printVariable(9, "$DIMJOGANG", 40, 0.7853981633974483);
        printVariable(9, "$DIMTFILL", 70, 0);
        printVariable(9, "$DIMTFILLCLR", 70, 0);
        printVariable(9, "$DIMARCSYM", 70, 0);
        printVariable(9, "$DIMLTYPE", 6, emptyString);
        printVariable(9, "$DIMLTEX1", 6, emptyString);
        printVariable(9, "$DIMLTEX2", 6, emptyString);
        printVariable(9, "$DIMTXTDIRECTION", 70, 0);
        printVariable(9, "$LUNITS", 70, 2);
        printVariable(9, "$LUPREC", 70, 4);
        printVariable(9, "$SKETCHINC", 40, 0.1);
        printVariable(9, "$FILLETRAD", 40, 0.5);
        printVariable(9, "$AUNITS", 70, 0);
        printVariable(9, "$AUPREC", 70, 0);
        printVariable(9, "$MENU", 1, ".");
        printVariable(9, "$ELEVATION", 40, 0.0);
        printVariable(9, "$PELEVATION", 40, 0.0);
        printVariable(9, "$THICKNESS", 40, 0.0);
        printVariable(9, "$LIMCHECK", 70, 0);
        printVariable(9, "$CHAMFERA", 40, 0.0);
        printVariable(9, "$CHAMFERB", 40, 0.0);
        printVariable(9, "$CHAMFERC", 40, 0.0);
        printVariable(9, "$CHAMFERD", 40, 0.0);
        printVariable(9, "$SKPOLY", 70, 0);
        printVariable(9, "$TDCREATE", 40, 2457928.916158947);
        printVariable(9, "$TDUCREATE", 40, 2457928.832825613);
        printVariable(9, "$TDUPDATE", 40, 2457928.91615897);
        printVariable(9, "$TDUUPDATE", 40, 2457928.832825636);
        printVariable(9, "$TDINDWG", 40, 0.0000000116);
        printVariable(9, "$TDUSRTIMER", 40, 0.0000000116);
        printVariable(9, "$USRTIMER", 70, 1);
        printVariable(9, "$ANGBASE", 50, 0.0);
        printVariable(9, "$ANGDIR", 70, 0);
        printVariable(9, "$PDMODE", 70, 0);
        printVariable(9, "$PDSIZE", 40, 0.0);
        printVariable(9, "$PLINEWID", 40, 0.0);
        printVariable(9, "$SPLFRAME", 70, 0);
        printVariable(9, "$SPLINETYPE", 70, 6);
        printVariable(9, "$SPLINESEGS", 70, 8);
        printVariable(9, "$HANDSEED", 5, "11E");
        printVariable(9, "$SURFTAB1", 70, 6);
        printVariable(9, "$SURFTAB2", 70, 6);
        printVariable(9, "$SURFTYPE", 70, 6);
        printVariable(9, "$SURFU", 70, 6);
        printVariable(9, "$SURFV", 70, 6);
        printVariable(9, "$UCSBASE", 2, emptyString);
        printVariable(9, "$UCSNAME", 2, emptyString);
        printVariable(9, "$UCSORG", 10, 0.0, 20, 0.0, 30, 0.0);
        printVariable(9, "$UCSXDIR", 10, 1.0,20, 0.0, 30, 0.0);
        printVariable(9, "$UCSYDIR", 10, 0.0,20, 1.0, 30, 0.0);
        printVariable(9, "$UCSORTHOREF", 2, emptyString);
        printVariable(9, "$UCSORTHOVIEW", 70, 0);
        printVariable(9, "$UCSORGTOP", 10, 0.0,20, 0.0, 30, 0.0);
        printVariable(9, "$UCSORGBOTTOM", 10, 0.0,20, 0.0, 30, 0.0);
        printVariable(9, "$UCSORGLEFT", 10, 0.0,20, 0.0, 30, 0.0);
        printVariable(9, "$UCSORGRIGHT", 10, 0.0,20, 0.0, 30, 0.0);
        printVariable(9, "$UCSORGFRONT", 10, 0.0,20, 0.0, 30, 0.0);
        printVariable(9, "$UCSORGBACK", 10, 0.0,20, 0.0, 30, 0.0);
        printVariable(9, "$PUCSBASE", 2, emptyString);
        printVariable(9, "$PUCSNAME", 2, emptyString);
        printVariable(9, "$PUCSORG", 10, 0.0,20, 0.0, 30, 0.0);
        printVariable(9, "$PUCSXDIR", 10, 1.0,20, 0.0, 30, 0.0);
        printVariable(9, "$PUCSYDIR", 10, 0.0,20, 1.0, 30, 0.0);
        printVariable(9, "$PUCSORTHOREF", 2, emptyString);
        printVariable(9, "$PUCSORTHOVIEW", 70, 0);
        printVariable(9, "$PUCSORGTOP", 10, 0.0,20, 0.0, 30, 0.0);
        printVariable(9, "$PUCSORGBOTTOM", 10, 0.0,20, 0.0, 30, 0.0);
        printVariable(9, "$PUCSORGLEFT", 10, 0.0,20, 0.0, 30, 0.0);
        printVariable(9, "$PUCSORGRIGHT", 10, 0.0,20, 0.0, 30, 0.0);
        printVariable(9, "$PUCSORGFRONT", 10, 0.0,20, 0.0, 30, 0.0);
        printVariable(9, "$PUCSORGBACK", 10, 0.0,20, 0.0, 30, 0.0);
        printVariable(9, "$USERI1", 70, 0);
        printVariable(9, "$USERI2", 70, 0);
        printVariable(9, "$USERI3", 70, 0);
        printVariable(9, "$USERI4", 70, 0);
        printVariable(9, "$USERI5", 70, 0);
        printVariable(9, "$USERR1", 40, 0.0);
        printVariable(9, "$USERR2", 40, 0.0);
        printVariable(9, "$USERR3", 40, 0.0);
        printVariable(9, "$USERR4", 40, 0.0);
        printVariable(9, "$USERR5", 40, 0.0);
        printVariable(9, "$WORLDVIEW", 70, 1);
        printVariable(9, "$SHADEDGE", 70, 3);
        printVariable(9, "$SHADEDIF", 70, 70);
        printVariable(9, "$TILEMODE", 70, 1);
        printVariable(9, "$MAXACTVP", 70, 32);
        printVariable(9, "$PINSBASE", 10, 0.0,20, 0.0, 30, 0.0);
        printVariable(9, "$PLIMCHECK", 70, 0);
        printVariable(9, "$PEXTMIN", 10, 0.0,20, 0.0, 30, 0.0);
        printVariable(9, "$PEXTMAX", 10, 0.0,20, 0.0, 30, 0.0);
        printVariable(9, "$PLIMMIN", 10, -20.0,20, -7.5);
        printVariable(9, "$PLIMMAX",10, 277.0, 20, 202.5);
        printVariable(9, "$UNITMODE", 70, 0);
        printVariable(9, "$VISRETAIN", 70, 1);
        printVariable(9, "$PLINEGEN", 70, 0);
        printVariable(9, "$PSLTSCALE", 70, 1);
        printVariable(9, "$TREEDEPTH", 70, 3020);
        printVariable(9, "$CMLSTYLE", 2, "Standard");
        printVariable(9, "$CMLJUST", 70, 0);
        printVariable(9, "$CMLSCALE", 40, 1.0);
        printVariable(9, "$PROXYGRAPHICS", 70, 1);
        printVariable(9, "$MEASUREMENT", 70, 1);
        printVariable(9, "$CELWEIGHT", 370, -1);
        printVariable(9, "$ENDCAPS", 280, 0);
        printVariable(9, "$JOINSTYLE", 280, 0);
        printVariable(9, "$LWDISPLAY", 290, 0);
        printVariable(9, "$INSUNITS", 70, 4);
        printVariable(9, "$HYPERLINKBASE", 1, emptyString);
        printVariable(9, "$STYLESHEET", 1, emptyString);
        printVariable(9, "$XEDIT", 290, 1);
        printVariable(9, "$CEPSNTYPE", 380, 0);
        printVariable(9, "$PSTYLEMODE", 290, 1);
        printVariable(9, "$FINGERprintVariableGUID", 2, "{09C53C9B - 40A1 - 4D42 - 8825 - E4B6CE79FF15}");
        printVariable(9, "$VERSIONGUID", 2, "{FAEB1C32 - E019 - 11D5 - 929B - 00C0DF256EC4}");
        printVariable(9, "$EXTNAMES", 290, 1);
        printVariable(9, "$PSVPSCALE", 40, 0.0);
        printVariable(9, "$OLESTARTUP", 290, 0);
        printVariable(9, "$SORTENTS", 280, 127);
        printVariable(9, "$INDEXCTL", 280, 0);
        printVariable(9, "$HIDETEXT", 280, 1);
        printVariable(9, "$XCLIPFRAME", 280, 2);
        printVariable(9, "$HALOGAP", 280, 0);
        printVariable(9, "$OBSCOLOR", 70, 257);
        printVariable(9, "$OBSLTYPE", 280, 0);
        printVariable(9, "$INTERSECTIONDISPLAY", 280, 0);
        printVariable(9, "$INTERSECTIONCOLOR", 70, 257);
        printVariable(9, "$DIMASSOC", 280, 2);
        printVariable(9, "$PROJECTNAME", 1, emptyString);
        printVariable(9, "$CAMERADISPLAY", 290, 0);
        printVariable(9, "$LENSLENGTH", 40, 50.0);
        printVariable(9, "$CAMERAHEIGHT", 40, 0.0);
        printVariable(9, "$STEPSPERSEC", 40, 2.0);
        printVariable(9, "$STEPSIZE", 40, 6.0);
        printVariable(9, "$3DDWFPREC", 40, 2.0);
        printVariable(9, "$PSOLWIDTH", 40, 0.25);
        printVariable(9, "$PSOLHEIGHT", 40, 4.0);
        printVariable(9, "$LOFTANG1", 40, 1.570796326794897);
        printVariable(9, "$LOFTANG2", 40, 1.570796326794897);
        printVariable(9, "$LOFTMAG1", 40, 0.0);
        printVariable(9, "$LOFTMAG2", 40, 0.0);
        printVariable(9, "$LOFTPARAM", 70, 7);
        printVariable(9, "$LOFTNORMALS", 280, 1);
        printVariable(9, "$LATITUDE", 40, 37.795);
        printVariable(9, "$LONGITUDE", 40, -122.394);
        printVariable(9, "$NORTHDIRECTION", 40, 0.0);
        printVariable(9, "$TIMEZONE", 70, -8000);
        printVariable(9, "$LIGHTGLYPHDISPLAY", 280, 1);
        printVariable(9, "$TILEMODELIGHTSYNCH", 280, 1);
        printVariable(9, "$CMATERIAL", 347, "7B");
        printVariable(9, "$SOLIDHIST", 280, 0);
        printVariable(9, "$SHOWHIST", 280, 1);
        printVariable(9, "$DWFFRAME", 280, 2);
        printVariable(9, "$DGNFRAME", 280, 2);
        printVariable(9, "$REALWORLDSCALE", 290, 1);
        printVariable(9, "$INTERFERECOLOR", 62, 256);
        printVariable(9, "$CSHADOW", 280, 0);
        printVariable(9, "$SHADOWPLANELOCATION", 40, 0.0);
        printVariable(0, "ENDSEC");
    }
}