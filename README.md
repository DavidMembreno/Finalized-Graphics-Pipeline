\# Emergent Parasite Simulation on a Software Graphics Pipeline



This repository showcases an \*\*agent-based parasite simulation\*\* rendered entirely through a \*\*custom-built software graphics pipeline\*\* written in Java.



Hundreds of parasites move, collide, grow, and eliminate one another over time.  

No global controller dictates outcomes — \*\*population dynamics emerge naturally\*\* from local interactions.



What makes this project unique is that the simulation is not layered \*on top\* of graphics.



> \*\*The simulation \*is\* the graphics pipeline.\*\*



Every frame is produced through explicit rasterization, shading, and scan conversion, then exported and compiled into a final video.



---



\## The Payoff: Emergent Behavior in Motion



The final output of this project is a video assembled from hundreds of rendered frames:



\- Parasites move along procedural paths

\- Collisions trigger a \*\*50/50 probabilistic rule\*\*

&nbsp; - one parasite consumes the other

&nbsp; - the winner grows in size

&nbsp; - the loser is removed

\- Geometry, color, and motion evolve together



Frames are rendered deterministically and compiled into video using a separate Python tool, keeping rendering and encoding cleanly separated.



This visual result is designed to \*\*hook the viewer first\*\*, then invite deeper exploration into how graphics systems actually work.



---



\## Why This Is Interesting



Most graphics projects stop at “drawing something.”



This project goes further by demonstrating:

\- how a \*\*graphics pipeline can function as a simulation engine\*\*

\- how \*\*emergent behavior\*\* arises from simple probabilistic rules

\- how rendering, math, and systems design reinforce one another

\- how GPU-style concepts can be recreated \*entirely in software\*



Everything below explains \*\*how this result is built from first principles\*\*.



---



\##  Pipeline Overview



Math → Geometry → Rasterization → Shading → Scene → Simulation → Frames → Video





Each stage is implemented explicitly — no OpenGL, no hardware acceleration.



---



\## 1. Mathematical Foundations



The pipeline is built on a custom mathematical core.



\### Vectors and Matrices

\- `Vector`, `VectorAbstract`

\- `Matrix`, `MatrixAbstract`



Vectors represent positions, directions, and interpolated values.  

Matrices encode linear transformations and allow spatial operations to be composed cleanly.



\### Affine Transformations

\- `AffineTransformation`, `AffineTransformationAbstract`



Translation, rotation, and scaling are modeled using affine matrices, enabling chained transformations and consistent coordinate-space reasoning.



\### Curves, Complex Numbers, and Fractals

\- `BezierCurve`, `Curves`

\- `ComplexNumber`, `Mandelbrot`



These components demonstrate that the pipeline can render \*\*mathematically defined structures\*\*, not just static meshes.  

The Mandelbrot renderer stresses numerical iteration and color mapping.



---



\## 2. Color Theory and Shading



Color is treated as a first-class concept.



\### Color Representation

\- `Color`, `ColorAbstract`



Colors are represented explicitly (RGB) and interpolated across geometry, enabling gradients and smooth fills.



\### Shaders

\- `Shader`, `ShaderAbstract`



Shading logic is decoupled from geometry and rasterization, mirroring the programmable stages of modern GPU pipelines.



---



\## 3. Geometry and Rasterization



This is the heart of the renderer.



\### Triangles as the Core Primitive

\- `Triangle`, `TriangleAbstract`



All filled geometry is decomposed into triangles — stable, planar, and easy to interpolate across.



\### Scan Conversion and Rasterization

\- `ScanConvertLine`, `ScanConvertAbstract`



Rasterization is performed using \*\*scanline-based scan conversion\*\*:

1\. Triangle edges are identified

2\. Edge intersections are computed per scanline

3\. Horizontal spans are filled pixel-by-pixel



This exposes aliasing, edge cases, and discretization challenges normally hidden by hardware acceleration.



---



\## 4. Scene Composition



\### Scene Objects

\- `SceneObject`, `SceneObjectAbstract`



Scene objects bind together:

\- geometry

\- transformations

\- shading behavior



This abstraction enables complex scenes without obscuring how rendering actually works.



---



\## 5. Dynamic Simulation and Emergent Behavior



\### Particles

\- `Particle`



Particles are the minimal dynamic unit, evolving over time via position and velocity updates.



\### Parasites (Agent-Based Simulation)

\- `Parasite`



Parasites extend particles with internal state and interaction rules.



When two parasites collide:

\- a \*\*50/50 probabilistic decision\*\* is applied

\- one parasite consumes the other

\- the winner grows

\- the loser is removed



No global rules dictate outcomes — \*\*emergence arises from repetition\*\*.



\### Procedural Motion

\- `Pathmaker`



Motion paths are generated algorithmically, producing smooth, reproducible trajectories without hardcoded movement.



---



\## 6. Asset Ingestion and Frame Output



\### STL Parsing

\- `STLParser`



STL files (triangle-based meshes) are parsed and rendered through the same pipeline as procedural geometry.



\### Frame Export

\- `ReadWriteImage`



Rendering occurs off-screen into image buffers.  

Each frame can be exported deterministically, making simulation capture reliable and reproducible.



---



\## 7. Executable Experiments (Tests as Entry Points)



This project treats tests as \*\*executable experiments\*\*, not unit tests.



Notable demos include:

\- `ShaderTest` – shading and fill styles

\- `TestColorPalette` – color interpolation

\- `CurvesTest`, `TestSinglePath` – parametric curves

\- `TestPathmakerRender` – procedural motion

\- `MandelbrotTest` – fractal rendering

\- `RandomWalk`, `CatRotator`, `STLRenderer` – focused visual demos



\### Parasite Simulations

\- `TestParasiteGame`

\- `TestParasiteDebugGame`

\- `TestHundredParasites`



`TestHundredParasites` runs a large-scale simulation and exports each frame for video generation.



---



\## Video Generation Tooling



Rendered frames are assembled into video using a separate Python script:



\- `tools/video\_export/Fil1.py`



This tool reads sequential `frameXXX.png` images and encodes them into an AVI file, keeping rendering and encoding concerns separate.



---



\## How to Run



1\. Open the project in \*\*IntelliJ IDEA\*\*

2\. Run any class with a `main` method in:

&nbsp;  - `src/Main`

&nbsp;  - `src/Tests`

3\. For the full simulation:

&nbsp;  - Run `TestHundredParasites`

&nbsp;  - Export frames

&nbsp;  - Run `Fil1.py` to generate the video



---



\## Final Note



This repository is best understood by \*\*running it\*\*, not just reading it.



The parasite simulation is the hook.  

The graphics pipeline is the engine that makes it possible.







