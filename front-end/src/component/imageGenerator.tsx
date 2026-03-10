// src/components/ImageGenerator.tsx
import React, { useEffect, useState } from 'react';
import { useAppContext } from '../context/AppContext';

interface ImageGeneratorProps {
  onClose: () => void;
}

export const ImageGenerator: React.FC<ImageGeneratorProps> = ({ onClose }) => {
  const { bills, calculations, totalBills, totalSalary, totalReserve } = useAppContext();
  const [imageUrl, setImageUrl] = useState('');

  const BRL = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' });
  const PC = new Intl.NumberFormat('pt-BR', { maximumFractionDigits: 2, minimumFractionDigits: 2 });

  const wrapText = (
    ctx: CanvasRenderingContext2D,
    text: string,
    maxWidth: number,
    font: string
  ): string[] => {
    ctx.font = font;
    const words = String(text).split(/\s+/);
    let line = '',
      lines: string[] = [];

    for (const w of words) {
      const test = line ? line + ' ' + w : w;
      if (ctx.measureText(test).width <= maxWidth) {
        line = test;
      } else {
        if (line) lines.push(line);
        if (ctx.measureText(w).width > maxWidth) {
          let chunk = '';
          for (const ch of w) {
            const test2 = chunk + ch;
            if (ctx.measureText(test2).width > maxWidth) {
              lines.push(chunk);
              chunk = ch;
            } else {
              chunk = test2;
            }
          }
          line = chunk;
        } else {
          line = w;
        }
      }
    }
    if (line) lines.push(line);
    return lines;
  };

  useEffect(() => {
    generateImage();
  }, []);

  const generateImage = () => {

    const numPeople = calculations.length;
    const minWidth = 900;
    const colWidth = 120;
    const W = Math.max(minWidth, 480 +(numPeople * colWidth));
  
    const pad = 28;
    const line = 28;

    const canvas = document.createElement('canvas');
    const ctx = canvas.getContext('2d')!;

    const fTitle = '700 22px ui-sans-serif, system-ui';
    const fLabel = '600 13px ui-sans-serif, system-ui';
    const fText = '400 15px ui-sans-serif, system-ui';
    const fHead = '700 12px ui-sans-serif, system-ui';
    const fRow = '400 14px ui-sans-serif, system-ui';

    const wConta = Math.min(420, W - pad * 2 - 140 - (numPeople * colWidth));

    // Calculate height
    let linesNeeded = 11; // base header
    bills.forEach((bill) => {
      const wrapped = wrapText(ctx, bill.description || '(sem nome)', wConta, fRow);
      linesNeeded += Math.max(1, wrapped.length);
    });
    linesNeeded += 8 + calculations.length * 2; // footer

    const H = pad * 2 + linesNeeded * line + 10;
    const dpr = Math.max(1, Math.min(3, window.devicePixelRatio || 1));

    canvas.width = W * dpr;
    canvas.height = H * dpr;
    ctx.scale(dpr, dpr);

    // Background
    ctx.fillStyle = '#ffffff';
    ctx.fillRect(0, 0, W, H);
    ctx.strokeStyle = '#e5e7eb';
    ctx.lineWidth = 1;
    ctx.strokeRect(0.5, 0.5, W - 1, H - 1);

    const title = (txt: string, x: number, y: number) => {
      ctx.fillStyle = '#111827';
      ctx.font = fTitle;
      ctx.fillText(txt, x, y);
    };
    const label = (txt: string, x: number, y: number) => {
      ctx.fillStyle = '#6b7280';
      ctx.font = fLabel;
      ctx.fillText(txt, x, y);
    };
    const text = (txt: string, x: number, y: number) => {
      ctx.fillStyle = '#111827';
      ctx.font = fText;
      ctx.fillText(txt, x, y);
    };
    const head = (txt: string, x: number, y: number) => {
      ctx.fillStyle = '#6b7280';
      ctx.font = fHead;
      ctx.fillText(txt, x, y);
    };
    const rowT = (txt: string, x: number, y: number) => {
      ctx.fillStyle = '#111827';
      ctx.font = fRow;
      ctx.fillText(txt, x, y);
    };

    let y = pad;

    title('Resumo de Contas Compartilhadas', pad, y);
    y += line;
    text(new Date().toLocaleString('pt-BR'), pad, y);
    y += line * 0.8;

    label('Participantes', pad, y);
    y += line;
    const names = calculations.map((c) => c.name).join(', ');
    text(names, pad, y);
    y += line;

    label('Salários', pad, y);
    y += line;
    const salaries = calculations
      .map((c) => `${c.name}: ${BRL.format(c.salary)}`)
      .join('  |  ');
    text(`${salaries}  |  Total: ${BRL.format(totalSalary)}`, pad, y);
    y += line;

    label('Participação nos custos', pad, y);
    y += line;
    const percentages = calculations
      .map((c) => `${c.name}: ${PC.format(c.percentage * 100)}%`)
      .join('  |  ');
    text(percentages, pad, y);
    y += line;

    y += line * 0.3;
    label('Contas (rateadas pela participação)', pad, y);
    y += line;

    // Table header
    const xConta = pad;
    const xTot = xConta + wConta;

    head('Conta', xConta, y);
    head('Total', xTot, y);

    calculations.forEach((calc, idx) => {
        const xPerson = xTot + 140 + (idx *colWidth);
        head(calc.name, xPerson, y);
    });
    y += line;

    // Bills rows
    bills.forEach((bill) => {
      const contaLines = wrapText(ctx, bill.description || '(sem nome)', wConta, fRow);
      contaLines.forEach((ln, idx) => {
        rowT(ln, xConta, y + idx * line);
      });
      rowT(BRL.format(bill.totalAmount), xTot, y);
      
      // Valores de cada pessoa
      calculations.forEach((calc, idx) => {
        const amount = bill.totalAmount * calc.percentage;
        const xPerson = xTot + 140 + (idx * colWidth);
        rowT(BRL.format(amount), xPerson, y);
      });
      
      y += Math.max(1, contaLines.length) * line;
    });

    y += line * 0.5;
    ctx.strokeStyle = '#e5e7eb';
    ctx.beginPath();
    ctx.moveTo(pad, y - 18);
    ctx.lineTo(W - pad, y - 18);
    ctx.stroke();

    label('Totais', pad, y);
    y += line;
    const billTotals = calculations
      .map((c) => `${c.name}: ${BRL.format(c.billsTotal)}`)
      .join(' | ');
    rowT(`Total das contas: ${BRL.format(totalBills)} | ${billTotals}`, pad, y);
    y += line;

    const reserves = calculations
      .map((c) => `${c.name} ${c.reservePercentage}% (${BRL.format(c.reserveAmount)})`)
      .join(' | ');
    rowT(`Reserva: ${reserves} | Total: ${BRL.format(totalReserve)}`, pad, y);
    y += line;

    y += line * 0.3;
    label('Conta + Reserva', pad, y);
    y += line;
    ctx.fillStyle = '#111827';
    ctx.font = '700 18px ui-sans-serif, system-ui';
    const totalsWithReserve = calculations
      .map((c) => `${c.name}: ${BRL.format(c.totalWithReserve)}`)
      .join('  |  ');
    ctx.fillText(totalsWithReserve, pad, y);
    y += line * 1.2;

    label('Sobras', pad, y);
    y += line;
    ctx.fillStyle = '#16a34a';
    ctx.font = '700 20px ui-sans-serif, system-ui';
    const remaining = calculations
      .map((c) => `${c.name}: ${BRL.format(c.remaining)}`)
      .join('  |  ');
    ctx.fillText(remaining, pad, y);

    const url = canvas.toDataURL('image/png');
    setImageUrl(url);
  };

  const baixarImagem = () => {
    const a = document.createElement('a');
    const stamp = new Date().toISOString().slice(0, 19).replace(/[:T]/g, '-');
    a.download = `divisao-contas-${stamp}.png`;
    a.href = imageUrl;
    a.click();
  };

  const copiarImagem = async () => {
    try {
      const response = await fetch(imageUrl);
      const blob = await response.blob();
      if (navigator.clipboard && window.ClipboardItem) {
        await navigator.clipboard.write([new ClipboardItem({ 'image/png': blob })]);
        alert('Imagem copiada!');
      } else {
        baixarImagem();
      }
    } catch (e) {
      baixarImagem();
    }
  };

  return (
    <div
      className="fixed inset-0 bg-black/45 flex items-center justify-center p-5 z-50"
      onClick={onClose}
    >
      <div
        className="bg-white rounded-2xl max-w-[900px] w-full p-4 shadow-2xl"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="flex justify-between items-center mb-2">
          <h3 className="text-lg font-semibold m-0">Pré-visualização</h3>
          <div className="flex gap-2">
            <button onClick={copiarImagem}>Copiar imagem</button>
            <button className="secondary" onClick={baixarImagem}>
              Baixar PNG
            </button>
            <button className="secondary" onClick={onClose}>
              Fechar
            </button>
          </div>
        </div>
        <div className="flex justify-center items-center p-2 bg-[#f9fafb] border border-[#e5e7eb] rounded-xl overflow-auto max-h-[70vh]">
          <img src={imageUrl} alt="Resumo em imagem" className="max-w-full h-auto" />
        </div>
      </div>
    </div>
  );
};