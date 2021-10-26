using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;
using System.IO;

public class NewAttempt : MonoBehaviour
{

    public AnimationClip clip;
    public string inputDataPath;
    public float BPM, lengthOfSongInSeconds;
    public float PRECISION;


    // The Notes To Animate
    public GameObject[] RED_ARROWS;
    public GameObject[] BLUE_ARROWS;
    public GameObject[] RED_DOTS;
    public GameObject[] BLUE_DOTS;

    // The Walls To Animate
    // TODO

    // The Lasers To Animate
    // TODO

    // The Rings To Animate
    // TODO

    private Keyframe TimeStart, TimeEnd, RotationStart, RotationEnd, rX, rY, rZ, pX, pY, pZ;
    private StreamReader sr;

    private Block note;
    private ArrayList RawData, Blocks, NEWLIST;

    private ArrayList RedArrows, BlueArrows, RedDots, BlueDots;
    private ArrayList block0, block1, block2, block3, block4, block5, block6, block7, block8, block9, block10, block11;


    private AnimationCurve RedArrowRot, BlueArrowRot, RedDotRot, BlueDotRot;
    private AnimationCurve RedArrowStart, BlueArrowStart, RedDotStart, BlueDotStart;

    private AnimationCurve RedArrowRotX, RedArrowRotY, RedArrowRotZ, RedArrowPosX, RedArrowPosY, RedArrowPosZ;
    private AnimationCurve BlueArrowRotX, BlueArrowRotY, BlueArrowRotZ, BlueArrowPosX, BlueArrowPosY, BlueArrowPosZ;
    private AnimationCurve RedDotRotX, RedDotRotY, RedDotRotZ, RedDotPosX, RedDotPosY, RedDotPosZ;
    private AnimationCurve BlueDotRotX, BlueDotRotY, BlueDotRotZ, BlueDotPosX, BlueDotPosY, BlueDotPosZ;

    private Type M_Transform = typeof(Transform);
    private Type M_Particle = typeof(ParticleSystem);
    private Type M_Objects = typeof(GameObject);


    private string rotationX = "m_LocalEulerAngles.x";
    private string rotationY = "m_LocalEulerAngles.y";
    private string rotationZ = "m_LocalEulerAngles.z";
    private string positionX = "m_LocalPosition.x";
    private string positionY = "m_LocalPosition.y";
    private string positionZ = "m_LocalPosition.z";
    private string setActive = "m_IsActive";
    private string rotation = "InitialModule.startRotation.scalar";

    private string[] paths = new string[4] {"World Constraint/Container/BeatSaberGame/Blocks/Red.Arrows/Red.Arrow.", 
        "World Constraint/Container/BeatSaberGame/Blocks/Blue.Arrows/Blue.Arrow.",
        "World Constraint/Container/BeatSaberGame/Blocks/Red.Dots/Red.Dot.",
        "World Constraint/Container/BeatSaberGame/Blocks/Blue.Dots/Blue.Dot." };

    private float[] Directions;

    private float DirUP = 180F;            // 0
    private float DirDOWN = 0F;               // 1
    private float DirLEFT = 90F;          // 2
    private float DirRIGHT = -90F;        // 3
    private float DirUP_LEFT = 135F;       // 4
    private float DirUP_RIGHT = -135F;     // 5
    private float DirDOWN_LEFT = 45F;     // 6
    private float DirDOWN_RIGHT = -45F;   // 7

    private float one4th = 4F;
    private float one8th = 8F;
    private float one16th = 16F;
    private float one32nd = 32F;
    private float one64th = 64F;

    private float FPS;

    private float beatsPerSecond, totalBeatsInSong, lengthInSecOfSingleMeasure, precision64th, keyFramesPerSecond, keyEndOffset, endKeyFrameOffset;


    // Layer Y Height
    private float Layer0 = 0.25F;
    private float Layer1 = 0.65F;
    private float Layer2 = 1.05F;

    // Lane X Offset
    private float Left = -0.6F;
    private float LeftMid = -0.2F;
    private float RightMid = 0.2F;
    private float Right = 0.6F;

    // Default Z Position
    private float ZPos = 10F;

    float[] layers, lanes;


    void Start()
    {
        startProgram();
    }

    private void startProgram()
    {
        // Timing Based Variables
        beatsPerSecond = BPM / 60F;
        totalBeatsInSong = beatsPerSecond * lengthOfSongInSeconds;
        lengthInSecOfSingleMeasure = 1F / beatsPerSecond;
        precision64th = lengthInSecOfSingleMeasure / one64th;
        keyFramesPerSecond = beatsPerSecond / precision64th;
        keyEndOffset = 2.0F;

        print("FPS at " + PRECISION + " Precision is " + getFps());

        // Float Values For Directions A Block Can Be In
        Directions = new float[9];
        Directions[0] = DirUP;
        Directions[1] = DirDOWN;
        Directions[2] = DirLEFT;
        Directions[3] = DirRIGHT;
        Directions[4] = DirUP_LEFT;
        Directions[5] = DirUP_RIGHT;
        Directions[6] = DirDOWN_LEFT;
        Directions[7] = DirDOWN_RIGHT;
        Directions[8] = 0F;

        layers = new float[3] { Layer0, Layer1, Layer2 };
        lanes = new float[4] { Left, LeftMid, RightMid, Right };


        // Initiate 4 block type arrays
        RedArrows = new ArrayList();
        BlueArrows = new ArrayList();
        RedDots = new ArrayList();
        BlueDots = new ArrayList();
        RawData = new ArrayList();

        // Step 1 - text input to ArrayList
        getInputDataIntoArrays(inputDataPath);

        // Step 2 - sort by block type into 4 arrays
        //private ArrayList RedArrows, BlueArrows, RedDots, BlueDots;
        sortByBlockTypeInto4Arrays(RawData);

        // Send each into method to set the BLOCK VALUE by it's time for later to set anis
        RedArrows = setAllTheseBlocksCount(RedArrows);
        //BlueArrows = setAllTheseBlocksCount(BlueArrows);
        //RedDots = setAllTheseBlocksCount(RedDots);
        //BlueDots = setAllTheseBlocksCount(BlueDots);

        // Creat Keyframes for all of these blocks and asign them the key
        createTheseBlocksKeyframes(RedArrows, 0);
        //createTheseBlocksKeyframes(BlueArrows, 1);
        //createTheseBlocksKeyframes(RedDots, 2);
        //createTheseBlocksKeyframes(BlueDots, 3);
    }

    private void getInputDataIntoArrays(string input)
    {
        sr = new StreamReader(input);
        string pass, line;
        int i = 0;
        while ((line = sr.ReadLine()) != null)
        {

            if (line.Contains("_time"))
            {
                note = new Block();
                pass = line;
                int startIndex = pass.IndexOf(":");//10
                int endIndex = pass.IndexOf(',');//changes
                int lengthOfString = endIndex - startIndex;
                pass = pass.Substring(startIndex + 1, lengthOfString - 1);
                note.setStartTime(float.Parse(pass));
                note.setEndTime(float.Parse(pass));
            }
            else if (line.Contains("_lineIndex"))
            {
                pass = line;
                pass = pass.Substring(13, 1);
                note.setLane(float.Parse(pass));
            }
            else if (line.Contains("_lineLayer"))
            {
                pass = line;
                pass = pass.Substring(13, 1);
                note.setLayer(float.Parse(pass));
            }
            else if (line.Contains("_type"))
            {
                pass = line;
                pass = pass.Substring(8, 1);
                note.setType(float.Parse(pass));
            }
            else if (line.Contains("_cutDirection"))
            {
                pass = line;
                pass = pass.Substring(16, 1);
                note.setCut(float.Parse(pass));
                RawData.Add(note);
            }
            else
            {
                i++;
                print("Line Null " + i);
            }

        }
        RawData.TrimToSize();
        sr.Close();
    }

    private void sortByBlockTypeInto4Arrays(ArrayList INPUT)
    {
        foreach (Block n in INPUT)
        {
            if (n.getType() == 0)//RED
            {
                if (n.getCut() == 8)//DOT
                {
                    RedDots.Add(n);
                }
                else if (n.getCut() != 8)//Normal RED
                {
                    RedArrows.Add(n);
                }
            }
            else if (n.getType() == 1)//BLUE
            {
                if (n.getCut() == 8)//DOT
                {
                    BlueDots.Add(n);
                }
                else if (n.getCut() != 8)//Normal BLUE
                {
                    BlueArrows.Add(n);
                }
            }
        }
        RedDots.TrimToSize();
        RedArrows.TrimToSize();
        BlueDots.TrimToSize();
        BlueArrows.TrimToSize();
    }

    private ArrayList setAllTheseBlocksCount(ArrayList INPUT)
    {
        int count = 0;
        foreach (Block n in INPUT)
        {
            if (count >= 8)
            {
                count = 0;
            }
            n.setCOUNT(count);
            count++;
        }
        return INPUT;
    }

    private float getFps()
    {
        FPS = beatsPerSecond * PRECISION;
        return FPS;
    }

    private void createTheseBlocksKeyframes(ArrayList INPUT, int TYPE)
    {
        float inW = getFps();
        //Block n in INPUT
        //int index = 0; index < 8; index++
        for (int index = 0; index < 8; index++)
        {

            // Rotation Curves
            RedArrowRot = new AnimationCurve();
            BlueArrowRot = new AnimationCurve();
            RedDotRot = new AnimationCurve();
            BlueDotRot = new AnimationCurve();

            // Time Start Curves
            RedArrowStart = new AnimationCurve();
            BlueArrowStart = new AnimationCurve();
            RedDotStart = new AnimationCurve();
            BlueDotStart = new AnimationCurve();

            // Position/Rotation Curves RedArrow
            RedArrowRotX = new AnimationCurve();
            RedArrowRotY = new AnimationCurve();
            RedArrowRotZ = new AnimationCurve();
            RedArrowPosX = new AnimationCurve();
            RedArrowPosY = new AnimationCurve();
            RedArrowPosZ = new AnimationCurve();

            // Position/Rotation Curves BlueArrow
            BlueArrowRotX = new AnimationCurve();
            BlueArrowRotY = new AnimationCurve();
            BlueArrowRotZ = new AnimationCurve();
            BlueArrowPosX = new AnimationCurve();
            BlueArrowPosY = new AnimationCurve();
            BlueArrowPosZ = new AnimationCurve();

            // Position/Rotation Curves RedDot
            RedDotRotX = new AnimationCurve();
            RedDotRotY = new AnimationCurve();
            RedDotRotZ = new AnimationCurve();
            RedDotPosX = new AnimationCurve();
            RedDotPosY = new AnimationCurve();
            RedDotPosZ = new AnimationCurve();

            // Position/Rotation Curves BlueDot
            BlueDotRotX = new AnimationCurve();
            BlueDotRotY = new AnimationCurve();
            BlueDotRotZ = new AnimationCurve();
            BlueDotPosX = new AnimationCurve();
            BlueDotPosY = new AnimationCurve();
            BlueDotPosZ = new AnimationCurve();

            float keyValue, startKeyValue, endKeyValue;
            //private Keyframe TimeStart, TimeEnd, X, Y, Z;

            // Give Animation A Default State Of All Blocks Off & Music Source On At First Note

            //Block n in INPUT
            //int index = 0; index < 8; index++
            foreach (Block n in INPUT)
            {
                if (n.getCOUNT() == index)
                {
                    keyValue = n.getStartTime();
                    startKeyValue = keyValue / beatsPerSecond;
                    endKeyValue = startKeyValue + keyEndOffset;
                    switch (TYPE)
                    {
                        //0 = RedArrow
                        //public GameObject[index] RED_ARROWS;
                        case 0:

                            // Time Start/End
                            TimeStart = new Keyframe(startKeyValue, 1F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            RedArrowStart.AddKey(TimeStart);
                            TimeEnd = new Keyframe(endKeyValue, 0F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            RedArrowStart.AddKey(TimeEnd);

                            // Particle Rotation Start - CutDirection
                            //RotationStart = new Keyframe(startKeyValue, Directions[n.getCut()])
                            //{
                            //    weightedMode = WeightedMode.Both,
                            //    inWeight = inW,
                            //    outWeight = 0F
                            //};
                            //RedArrowRot.AddKey(RotationStart);
                            //RotationEnd = new Keyframe(endKeyValue, 0F)
                            //{
                            //    weightedMode = WeightedMode.Both,
                            //    inWeight = inW,
                            //    outWeight = 0F
                            //};
                            //RedArrowRot.AddKey(RotationEnd);

                            // Rotation Set
                            rX = new Keyframe(startKeyValue - 0.1F, 0F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            RedArrowRotX.AddKey(rX);
                            rY = new Keyframe(startKeyValue - 0.1F, 180F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            RedArrowRotY.AddKey(rY);
                            rZ = new Keyframe(startKeyValue - 0.1F, Directions[n.getCut()]) // Value 2 was 0F
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            RedArrowRotZ.AddKey(rZ);

                            // Position Set
                            pX = new Keyframe(startKeyValue - 0.1F, lanes[n.getLane()]) // LANE
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            RedArrowPosX.AddKey(pX);
                            pY = new Keyframe(startKeyValue - 0.1F, layers[n.getLayer()]) // LAYER
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            RedArrowPosY.AddKey(pY);
                            pZ = new Keyframe(startKeyValue - 0.1F, 10F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            RedArrowPosZ.AddKey(pZ);
                            break;
                        //1 = BlueArrow
                        //public GameObject[] BLUE_ARROWS;
                        case 1:


                            // Time Start/End
                            TimeStart = new Keyframe(startKeyValue, 1F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            BlueArrowStart.AddKey(TimeStart);
                            TimeEnd = new Keyframe(endKeyValue, 0F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            BlueArrowStart.AddKey(TimeEnd);

                            //// Particle Rotation Start - CutDirection
                            //RotationStart = new Keyframe(startKeyValue, Directions[n.getCut()])
                            //{
                            //    weightedMode = WeightedMode.Both,
                            //    inWeight = inW,
                            //    outWeight = 0F
                            //};
                            //BlueArrowRot.AddKey(RotationStart);
                            //RotationEnd = new Keyframe(endKeyValue, 0F)
                            //{
                            //    weightedMode = WeightedMode.Both,
                            //    inWeight = inW,
                            //    outWeight = 0F
                            //};
                            //BlueArrowRot.AddKey(RotationEnd);

                            // Rotation Set
                            rX = new Keyframe(startKeyValue - 0.1F, 0F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            BlueArrowRotX.AddKey(rX);
                            rY = new Keyframe(startKeyValue - 0.1F, 180F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            BlueArrowRotY.AddKey(rY);
                            rZ = new Keyframe(startKeyValue - 0.1F, Directions[n.getCut()]) // Value 2 was 0F
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            BlueArrowRotZ.AddKey(rZ);

                            // Position Set
                            pX = new Keyframe(startKeyValue - 0.1F, lanes[n.getLane()]) // LANE
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            BlueArrowPosX.AddKey(pX);
                            pY = new Keyframe(startKeyValue - 0.1F, layers[n.getLayer()]) // LAYER
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            BlueArrowPosY.AddKey(pY);
                            pZ = new Keyframe(startKeyValue - 0.1F, 10F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            BlueArrowPosZ.AddKey(pZ);
                            break;
                        //2 = RedDot
                        //public GameObject[] RED_DOTS;
                        case 2:

                            TimeStart = new Keyframe(startKeyValue, 1F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            RedDotStart.AddKey(TimeStart);
                            TimeEnd = new Keyframe(endKeyValue, 0F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            RedDotStart.AddKey(TimeEnd);

                            //// Particle Rotation Start - CutDirection
                            //RotationStart = new Keyframe(startKeyValue, Directions[n.getCut()])
                            //{
                            //    weightedMode = WeightedMode.Both,
                            //    inWeight = inW,
                            //    outWeight = 0F
                            //};
                            //RedDotRot.AddKey(RotationStart);
                            //RotationEnd = new Keyframe(endKeyValue, 0F)
                            //{
                            //    weightedMode = WeightedMode.Both,
                            //    inWeight = inW,
                            //    outWeight = 0F
                            //};
                            //RedDotRot.AddKey(RotationEnd);

                            // Rotation Set
                            rX = new Keyframe(startKeyValue - 0.1F, 0F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            RedDotRotX.AddKey(rX);
                            rY = new Keyframe(startKeyValue - 0.1F, 180F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            RedDotRotY.AddKey(rY);
                            rZ = new Keyframe(startKeyValue - 0.1F, Directions[n.getCut()]) // Value 2 was 0F
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            RedDotRotZ.AddKey(rZ);

                            // Position Set
                            pX = new Keyframe(startKeyValue - 0.1F, lanes[n.getLane()]) // LANE
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            RedDotPosX.AddKey(pX);
                            pY = new Keyframe(startKeyValue - 0.1F, layers[n.getLayer()]) // LAYER
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            RedDotPosY.AddKey(pY);
                            pZ = new Keyframe(startKeyValue - 0.1F, 10F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            RedDotPosZ.AddKey(pZ);
                            break;
                        //3 = BlueDot
                        //public GameObject[] BLUE_DOTS;
                        case 3:

                            TimeStart = new Keyframe(startKeyValue, 1F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            BlueDotStart.AddKey(TimeStart);
                            TimeEnd = new Keyframe(endKeyValue, 0F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            BlueDotStart.AddKey(TimeEnd);

                            //// Particle Rotation Start - CutDirection
                            //RotationStart = new Keyframe(startKeyValue, Directions[n.getCut()])
                            //{
                            //    weightedMode = WeightedMode.Both,
                            //    inWeight = inW,
                            //    outWeight = 0F
                            //};
                            //BlueDotRot.AddKey(RotationStart);
                            //RotationEnd = new Keyframe(endKeyValue, 0F)
                            //{
                            //    weightedMode = WeightedMode.Both,
                            //    inWeight = inW,
                            //    outWeight = 0F
                            //};
                            //BlueDotRot.AddKey(RotationEnd);

                            // Rotation Set
                            rX = new Keyframe(startKeyValue - 0.1F, 0F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            BlueDotRotX.AddKey(rX);
                            rY = new Keyframe(startKeyValue - 0.1F, 180F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            BlueDotRotY.AddKey(rY);
                            rZ = new Keyframe(startKeyValue - 0.1F, Directions[n.getCut()]) // Value 2 was 0F
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            BlueDotRotZ.AddKey(rZ);

                            // Position Set
                            pX = new Keyframe(startKeyValue - 0.1F, lanes[n.getLane()]) // LANE
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            BlueDotPosX.AddKey(pX);
                            pY = new Keyframe(startKeyValue - 0.1F, layers[n.getLayer()]) // LAYER
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            BlueDotPosY.AddKey(pY);
                            pZ = new Keyframe(startKeyValue - 0.1F, 10F)
                            {
                                weightedMode = WeightedMode.Both,
                                inWeight = -inW,
                                outWeight = 0F
                            };
                            BlueDotPosZ.AddKey(pZ);
                            break;
                    }
                }


                addKeysToCurveOfType(INPUT, TYPE, index, RedArrowStart, BlueArrowStart, RedDotStart, BlueDotStart,
               RedArrowRotX, RedArrowRotY, RedArrowRotZ, RedArrowPosX, RedArrowPosY, RedArrowPosZ, BlueArrowRotX, BlueArrowRotY, BlueArrowRotZ, BlueArrowPosX,
               BlueArrowPosY, BlueArrowPosZ, RedDotRotX, RedDotRotY, RedDotRotZ, RedDotPosX, RedDotPosY, RedDotPosZ, BlueDotRotX, BlueDotRotY, BlueDotRotZ,
               BlueDotPosX, BlueDotPosY, BlueDotPosZ);

            }


            //private ArrayList block0, block1, block2, block3, block4, block5, block6, block7, block8, block9, block10, block11;

            //addKeysToCurveOfType(INPUT, TYPE, index, RedArrowStart, BlueArrowStart, RedDotStart, BlueDotStart,
            //   RedArrowRotX, RedArrowRotY, RedArrowRotZ, RedArrowPosX, RedArrowPosY, RedArrowPosZ, BlueArrowRotX, BlueArrowRotY, BlueArrowRotZ, BlueArrowPosX,
            //   BlueArrowPosY, BlueArrowPosZ, RedDotRotX, RedDotRotY, RedDotRotZ, RedDotPosX, RedDotPosY, RedDotPosZ, BlueDotRotX, BlueDotRotY, BlueDotRotZ,
            //   BlueDotPosX, BlueDotPosY, BlueDotPosZ);

            //addKeysToCurveOfType(INPUT, TYPE, index, RedArrowRot, BlueArrowRot, RedDotRot, BlueDotRot, RedArrowStart, BlueArrowStart, RedDotStart, BlueDotStart,
            //   RedArrowRotX, RedArrowRotY, RedArrowRotZ, RedArrowPosX, RedArrowPosY, RedArrowPosZ, BlueArrowRotX, BlueArrowRotY, BlueArrowRotZ, BlueArrowPosX,
            //   BlueArrowPosY, BlueArrowPosZ, RedDotRotX, RedDotRotY, RedDotRotZ, RedDotPosX, RedDotPosY, RedDotPosZ, BlueDotRotX, BlueDotRotY, BlueDotRotZ,
            //   BlueDotPosX, BlueDotPosY, BlueDotPosZ);

        }

    }

    //private void addKeysToCurveOfType(ArrayList INPUT, int blockType, int blockIndex, AnimationCurve RAR, AnimationCurve BAR, AnimationCurve RDR, AnimationCurve BDR,
    //    AnimationCurve RAS, AnimationCurve BAS, AnimationCurve RDS, AnimationCurve BDS, AnimationCurve RARX, AnimationCurve RARY, AnimationCurve RARZ,
    //    AnimationCurve RAPX, AnimationCurve RAPY, AnimationCurve RAPZ, AnimationCurve BARX, AnimationCurve BARY, AnimationCurve BARZ,
    //    AnimationCurve BAPX, AnimationCurve BAPY, AnimationCurve BAPZ, AnimationCurve RDRX, AnimationCurve RDRY, AnimationCurve RDRZ,
    //    AnimationCurve RDPX, AnimationCurve RDPY, AnimationCurve RDPZ, AnimationCurve BDRX, AnimationCurve BDRY, AnimationCurve BDRZ,
    //    AnimationCurve BDPX, AnimationCurve BDPY, AnimationCurve BDPZ)

    //TYPE: 0 = RedArrow, 1 = BlueArrow, 2 = RedDot, 3 = BlueDot
    private void addKeysToCurveOfType(ArrayList INPUT, int blockType, int blockIndex,
        AnimationCurve RAS, AnimationCurve BAS, AnimationCurve RDS, AnimationCurve BDS, AnimationCurve RARX, AnimationCurve RARY, AnimationCurve RARZ,
        AnimationCurve RAPX, AnimationCurve RAPY, AnimationCurve RAPZ, AnimationCurve BARX, AnimationCurve BARY, AnimationCurve BARZ,
        AnimationCurve BAPX, AnimationCurve BAPY, AnimationCurve BAPZ, AnimationCurve RDRX, AnimationCurve RDRY, AnimationCurve RDRZ,
        AnimationCurve RDPX, AnimationCurve RDPY, AnimationCurve RDPZ, AnimationCurve BDRX, AnimationCurve BDRY, AnimationCurve BDRZ,
        AnimationCurve BDPX, AnimationCurve BDPY, AnimationCurve BDPZ)
    {

        foreach (Block n in INPUT)
        {
            switch (blockType)
            {
                
                //RedArrow
                case 0:
                    print("Block Type: Red Arrow." + n.getCOUNT() + ", time is: " + n.getStartTime());
                    print("Lane = " + n.getLane() + ", Layer = " + n.getLayer());
                    print("CutDirection = " + n.getCut());
                    //GameObject isActive TRUE/FALSE
                    clip.SetCurve(paths[blockType] + blockIndex, M_Objects, setActive, RAS);

                    //ParticleSystem Rotation CUT/ANGLE
                    //clip.SetCurve(paths[blockType] + blockIndex, M_Particle, rotation, RAR);

                    //Physical Rotation LANE/LAYER
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, rotationX, RARX);
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, rotationY, RARY);
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, rotationZ, RARZ);

                    //Physical Position LANE/LAYER
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, positionX, RAPX);
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, positionY, RAPY);
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, positionZ, RAPZ);
                    break;
                //BlueArrow
                case 1:
                    print("Block Type: Blue Arrow." + n.getCOUNT() + ", time is: " + n.getStartTime());
                    print("Lane = " + n.getLane() + ", Layer = " + n.getLayer());
                    print("CutDirection = " + n.getCut());
                    //GameObject isActive TRUE/FALSE
                    clip.SetCurve(paths[blockType] + blockIndex, M_Objects, setActive, BAS);

                    //ParticleSystem Rotation CUT/ANGLE
                    //clip.SetCurve(paths[blockType] + blockIndex, M_Particle, rotation, BAR);

                    //Physical Rotation LANE/LAYER
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, rotationX, BARX);
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, rotationY, BARY);
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, rotationZ, BARZ);

                    //Physical Position LANE/LAYER
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, positionX, BAPX);
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, positionY, BAPY);
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, positionZ, BAPZ);
                    break;
                //RedDot
                case 2:
                    print("Block Type: Red Dot." + n.getCOUNT() + ", time is: " + n.getStartTime());
                    print("Lane = " + n.getLane() + ", Layer = " + n.getLayer());
                    print("CutDirection = " + n.getCut());
                    //GameObject isActive TRUE/FALSE
                    clip.SetCurve(paths[blockType] + blockIndex, M_Objects, setActive, RDS);

                    //ParticleSystem Rotation CUT/ANGLE
                    //clip.SetCurve(paths[blockType] + blockIndex, M_Particle, rotation, RDR);

                    //Physical Rotation LANE/LAYER
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, rotationX, RDRX);
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, rotationY, RDRY);
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, rotationZ, RDRZ);

                    //Physical Position LANE/LAYER
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, positionX, RDPX);
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, positionY, RDPY);
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, positionZ, RDPZ);
                    break;
                //BlueDot
                case 3:
                    print("Block Type: Blue Dot." + n.getCOUNT() + ", time is: " + n.getStartTime());
                    print("Lane = " + n.getLane() + ", Layer = " + n.getLayer());
                    print("CutDirection = " + n.getCut());
                    //GameObject isActive TRUE/FALSE
                    clip.SetCurve(paths[blockType] + blockIndex, M_Objects, setActive, BDS);

                    //ParticleSystem Rotation CUT/ANGLE
                    //clip.SetCurve(paths[blockType] + blockIndex, M_Particle, rotation, BDR);

                    //Physical Rotation LANE/LAYER
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, rotationX, BDRX);
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, rotationY, BDRY);
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, rotationZ, BDRZ);

                    //Physical Position LANE/LAYER
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, positionX, BDPX);
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, positionY, BDPY);
                    clip.SetCurve(paths[blockType] + blockIndex, M_Transform, positionZ, BDPZ);
                    break;
            }

        }
        //NEWLIST.Clear();
    }
}
