# OpenGL 3D Interactive Scene: Desktop Environment

## Project Overview
This project is an interactive 3D graphical scene built using **C++** and **OpenGL**. The application renders a realistic desk setup comprising multiple distinct objects—including a wooden desk, a MacBook Air, an AirPods charging case, a Sony ICF-C1B alarm clock, and a custom 3D dinosaur. It incorporates advanced computer graphics concepts such as procedural texture mapping, material lighting, vertex and fragment shaders, and spatial transformations.

---

## Part 1: Skills Learned from Instructor's Provided Code
Before the custom implementation boundary, the starter code provided by the instructor established the foundational architecture of the graphics pipeline. By analyzing and working with this baseline code, I developed proficiency in the following areas:

*   **Graphics Pipeline & Window Initialization:** Understanding how to configure OpenGL contexts, manage viewport rendering loops, and interface with underlying rendering hardware.
*   **Shader Management (`ShaderManager`):** Learning how to pass uniform variables (matrices, colors, booleans, and texture samplers) from the CPU to GPU-bound vertex and fragment shaders.
*   **Texture Loading & Configuration (`stb_image`):** Mastering the loading of bitmap image files, flipping image data vertically for OpenGL coordinate conventions, generating mipmaps, and configuring wrapping/filtering parameters (`GL_REPEAT`, `GL_LINEAR`).
*   **Texture Slots & Binding:** Managing up to 16 texture memory slots concurrently using `glActiveTexture` and `glBindTexture` to bind multiple textures dynamically across different objects.
*   **Lighting Models & Material Properties:** Implementing Phong lighting components—ambient, diffuse, and specular strengths—alongside multi-source positional lighting configurations to simulate realistic illumination.
*   **Matrix Transformations (`GLM`):** Gaining fluency in composing model transformations using scaling (`glm::scale`), rotations (`glm::rotate` across X, Y, and Z axes), and translations (`glm::translate`) combined into a final model-view matrix.

---

## Part 2: Custom Work & Skills Demonstrated (Post-Student Modification Line)
Everything implemented below the student modification comment line represents my own custom 3D modeling, asset integration, and scene composition work.

### 1. Custom Object Modeling & Scene Composition
Instead of relying on primitive shapes alone, I combined and transformed basic meshes (`ShapeMeshes`) to construct recognizable real-world objects:
*   **Desk (`DrawDesk`):** Configured a wide base plane mesh scaled to act as a workspace surface, mapped with a seamless wooden texture and custom material properties (`planeMaterial`).
*   **MacBook Air (`DrawMacBookAir`):** Modeled a closed laptop utilizing a flattened, wide box mesh mapped with a black/metallic texture.
*   **AirPods Case (`DrawAirpods`):** Assembled a multi-part composite object using a central cylinder body, top and bottom half-spheres, and a thin separating cylinder line to meticulously replicate an AirPods charging case.
*   **Sony Alarm Clock (`DrawSonyAlarmClock`):** Constructed a digital clock body with a grey-metal texture, a glowing green display screen area, and dual top control buttons.
*   **3D Dinosaur (`DrawDinosaur`):** Designed an organic creature model by scaling and positioning a primary sphere body, four half-sphere feet, a tapered cylinder neck, a head sphere, and white spherical eyes.

### 2. Advanced Technical Competencies Applied
*   **Hierarchical Spatial Positioning:** Carefully calculating relative local/world coordinates (`glm::vec3`) to stack and align components without overlapping or clipping (e.g., placing clock buttons precisely on top of the alarm clock body, or aligning dinosaur feet relative to the body).
*   **Texture Mapping & UV Parameterization:** Applying diverse textures (`wood-seamless`, `grey-metal-seamless`, `black-seamless`, `white`, `green`) to appropriate object segments to maximize visual fidelity.
*   **Modular Function Design:** Encapsulating each scene element into dedicated, clean functions (`DrawDesk`, `DrawAirpods`, `DrawMacBookAir`, `DrawSonyAlarmClock`, `DrawDinosaur`) to maintain high code readability, reusability, and separation of concerns.