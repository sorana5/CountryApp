interface Props {
    message: string | null;
    onClose: () => void;
}

export default function ErrorAlert({ message, onClose }: Props) {
    if (!message) return null;
    return (
        <div style={{
            background: '#fee2e2', border: '1px solid #fecaca',
            color: '#b91c1c', padding: '12px 16px', borderRadius: '8px',
            marginBottom: '16px', display: 'flex',
            justifyContent: 'space-between', alignItems: 'center'
        }}>
            <span>{message}</span>
            <button onClick={onClose} style={{
                background: 'none', border: 'none',
                color: '#b91c1c', cursor: 'pointer', fontSize: '18px'
            }}>×</button>
        </div>
    );
}