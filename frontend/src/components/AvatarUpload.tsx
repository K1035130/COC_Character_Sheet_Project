import { useRef, useState } from 'react';
import type { DragEvent } from 'react';
import { useToast } from './Toast';

const MAX_DIMENSION = 400;
const JPEG_QUALITY = 0.82;

interface AvatarUploadProps {
  name: string;
  avatarDataUrl: string | null;
  onUpload: (dataUrl: string) => Promise<void>;
}

function resizeToDataUrl(file: File): Promise<string> {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.onerror = () => reject(new Error('Could not read file'));
    reader.onload = () => {
      const img = new Image();
      img.onerror = () => reject(new Error('Could not decode image'));
      img.onload = () => {
        const scale = Math.min(1, MAX_DIMENSION / Math.max(img.width, img.height));
        const canvas = document.createElement('canvas');
        canvas.width = Math.round(img.width * scale);
        canvas.height = Math.round(img.height * scale);
        const ctx = canvas.getContext('2d');
        if (!ctx) {
          reject(new Error('Canvas not supported'));
          return;
        }
        ctx.drawImage(img, 0, 0, canvas.width, canvas.height);
        resolve(canvas.toDataURL('image/jpeg', JPEG_QUALITY));
      };
      img.src = reader.result as string;
    };
    reader.readAsDataURL(file);
  });
}

export function AvatarUpload({ name, avatarDataUrl, onUpload }: AvatarUploadProps) {
  const toast = useToast();
  const inputRef = useRef<HTMLInputElement>(null);
  const [dragOver, setDragOver] = useState(false);
  const [uploading, setUploading] = useState(false);

  const handleFile = async (file: File | undefined) => {
    if (!file) {
      return;
    }
    if (!file.type.startsWith('image/')) {
      toast.error('Please choose an image file');
      return;
    }
    setUploading(true);
    try {
      const dataUrl = await resizeToDataUrl(file);
      await onUpload(dataUrl);
      toast.success('Avatar updated');
    } catch {
      toast.error('Could not upload avatar');
    } finally {
      setUploading(false);
    }
  };

  const handleDrop = (event: DragEvent<HTMLDivElement>) => {
    event.preventDefault();
    setDragOver(false);
    handleFile(event.dataTransfer.files[0]);
  };

  return (
    <div
      className={`avatar-upload${dragOver ? ' avatar-upload-dragover' : ''}`}
      onDragOver={(e) => {
        e.preventDefault();
        setDragOver(true);
      }}
      onDragLeave={() => setDragOver(false)}
      onDrop={handleDrop}
      onClick={() => inputRef.current?.click()}
      role="button"
      tabIndex={0}
    >
      {avatarDataUrl ? (
        <img src={avatarDataUrl} alt={`${name} avatar`} className="avatar-image" />
      ) : (
        <div className="avatar-placeholder">{name.charAt(0).toUpperCase()}</div>
      )}
      <div className="avatar-upload-hint">{uploading ? 'Uploading...' : 'Drop image or click'}</div>
      <input
        ref={inputRef}
        type="file"
        accept="image/*"
        hidden
        onChange={(e) => handleFile(e.target.files?.[0])}
      />
    </div>
  );
}
