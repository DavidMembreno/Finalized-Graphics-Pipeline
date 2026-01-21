import cv2
import os


def create_video(input_folder, output_video_file, frame_rate=24):
    images = []

    # Collect frames with filename format: frame000.png, frame001.png, ...

    for i in range (516):  # Adjust as needed
        image_path = os.path.join(input_folder, f'frame{i:03d}.png')
        if os.path.exists(image_path):
            images.append(image_path)
        else:
            break

    if not images:
        print("No frames found!")
        return

    # Load first frame to get video dimensions
    first_frame = cv2.imread(images[0])
    height, width, _ = first_frame.shape

    fourcc = cv2.VideoWriter_fourcc(*'DIVX')
    video_writer = cv2.VideoWriter(output_video_file, fourcc, frame_rate, (width, height))

    for image_path in images:
        frame = cv2.imread(image_path)
        video_writer.write(frame)

    video_writer.release()
    print(f"Video saved to {output_video_file}")


if __name__ == "__main__":
    input_folder = r'C:\Users\socce\IdeaProjects\Finalized Graphics Pipeline'  # Your folder with frameXXX.png images
    output_video_file = "ForwardParasites_Path_Not_Shown_animation.avi"  # Output video filename
    create_video(input_folder, output_video_file)
