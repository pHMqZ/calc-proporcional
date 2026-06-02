import React, { useEffect, useState } from 'react';
import { useAppContext } from '../context/AppContext';

interface ImageGeneratorProps {
  onClose: () => void;
}

export const ImageGenerator: React.FC<ImageGeneratorProps> = ({ onClose }) => {
  const { bills, summary } = useAppContext();
  const [imageUrl, setImageUrl] = useState('');

  const BRL = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' });
  const PC = new Intl.NumberFormat('pt-BR', { maximumFractionDigits: 1, minimumFractionDigits: 1 });

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
    if (summary) {
      generateImage();
    }
  }, [summary]);

  const generateImage = () => {
    if (!summary) return;

    const { peopleData, totalSalary, totalBills } = summary;
    const numPeople = peopleData.length;
    const minWidth = 950;
    const colWidth = 150;
    const W = Math.max(minWidth, 480 + (numPeople * colWidth));

    const pad = 40;
    const line = 32;

    const canvas = document.createElement('canvas');
    const ctx = canvas.getContext('2d')!;

    const fTitle = '700 26px ui-sans-serif, system-ui';
    const fSection = '700 18px ui-sans-serif, system-ui';
    const fLabel = '600 14px ui-sans-serif, system-ui';
    const fText = '400 16px ui-sans-serif, system-ui';
    const fBold = '700 16px ui-sans-serif, system-ui';
    const fHead = '700 13px ui-sans-serif, system-ui';
    const fRow = '400 15px ui-sans-serif, system-ui';

    const wConta = Math.min(450, W - pad * 2 - 160 - (numPeople * colWidth));

    let simulatedY = pad + 15;

    //Título e data
    simulatedY += line * 3;

    //Participantes, Rateio e Salários integrados
    simulatedY += line;        // section('PARTICIPANTES E RATEIO')
    simulatedY += line;        // proporções de rateio
    simulatedY += line;        // label('Salários')
    simulatedY += line;        // valores de salários
    simulatedY += line * 1.5;  // margem pós-bloco

    //Detalhamento de contas (Cabeçalho)
    simulatedY += line + line * 0.5 + line;

    //Linhas de contas
    bills.forEach((bill) => {
      const billLines = wrapText(ctx, bill.description || '(sem nome)', wConta, fRow);

      simulatedY += Math.max(1, billLines.length) * line;
    });

    //Espaçamento
    simulatedY += line * 2;

    //Valores de reserva
    simulatedY += line;
    peopleData.forEach(() => {
      simulatedY += line;
    });

    //Resumo final
    simulatedY += line * 1.2 + line;
    peopleData.forEach(() => {
      simulatedY += line;
    });

    //Saldo restante
    simulatedY += line * 1.2 + line;
    peopleData.forEach(() => {
      simulatedY += line;
    });

    //Altura final
    const H = simulatedY + pad;
    const dpr = 2;

    canvas.width = W * dpr;
    canvas.height = H * dpr;
    ctx.scale(dpr, dpr);

    // Background
    ctx.fillStyle = '#ffffff';
    ctx.fillRect(0, 0, W, H);

    // Draw Border
    ctx.strokeStyle = '#f3f4f6';
    ctx.lineWidth = 15;
    ctx.strokeRect(7.5, 7.5, W - 15, H - 15);

    const title = (txt: string, x: number, y: number) => {
      ctx.fillStyle = '#111827';
      ctx.font = fTitle;
      ctx.fillText(txt, x, y);
    };
    const section = (txt: string, x: number, y: number) => {
      ctx.fillStyle = '#111827';
      ctx.font = fSection;
      ctx.fillText(txt.toUpperCase(), x, y);
    };
    const label = (txt: string, x: number, y: number) => {
      ctx.fillStyle = '#6b7280';
      ctx.font = fLabel;
      ctx.fillText(txt, x, y);
    };
    const text = (txt: string, x: number, y: number) => {
      ctx.fillStyle = '#374151';
      ctx.font = fText;
      ctx.fillText(txt, x, y);
    };
    const boldText = (txt: string, x: number, y: number, color = '#111827') => {
      ctx.fillStyle = color;
      ctx.font = fBold;
      ctx.fillText(txt, x, y);
    };
    const head = (txt: string, x: number, y: number) => {
      ctx.fillStyle = '#9ca3af';
      ctx.font = fHead;
      ctx.fillText(txt, x, y);
    };
    const rowT = (txt: string, x: number, y: number) => {
      ctx.fillStyle = '#1f2937';
      ctx.font = fRow;
      ctx.fillText(txt, x, y);
    };

    let y = pad + 15;

    title('Relatório de Contas Compartilhadas', pad + 10, y);
    y += line;
    text(new Date().toLocaleDateString('pt-BR', { dateStyle: 'long' }), pad + 10, y);
    y += line * 2;

    section('Participantes e Rateio', pad + 10, y);
    y += line;

    //Proporções
    const participants = peopleData
      .map((d) => `${d.person.name}: ${PC.format(d.percentage)}%`)
      .join('   |   ');
    text(participants, pad + 10, y);
    y += line;

    //Salários
    label('Salários', pad + 10, y);
    y += line;

    const salarieText = peopleData
      .map((d) => `${d.person.name}: ${BRL.format(d.person.salary)}`)
      .join('   |   ') + ` | Total: ${BRL.format(totalSalary)}`;
    text(salarieText, pad + 10, y);
    y += line * 1.5;

    section('Detalhamento das Contas', pad + 10, y);
    y += line;

    // Table header
    const xConta = pad + 10;
    const xTot = xConta + wConta;

    head('Conta', xConta, y);
    head('Valor Total', xTot, y);

    peopleData.forEach((data, idx) => {
      const xPerson = xTot + 140 + (idx * colWidth);
      head(data.person.name.toUpperCase(), xPerson, y);
    });
    y += line * 0.5;

    ctx.strokeStyle = '#f3f4f6';
    ctx.lineWidth = 1;
    ctx.beginPath();
    ctx.moveTo(pad + 10, y + 5);
    ctx.lineTo(W - pad - 10, y + 5);
    ctx.stroke();
    y += line;

    // Bills rows
    bills.forEach((bill) => {
      const contaLines = wrapText(ctx, bill.description || '(sem nome)', wConta, fRow);
      contaLines.forEach((ln, idx) => {
        rowT(ln, xConta, y + idx * line);
      });
      rowT(BRL.format(bill.totalAmount), xTot, y);

      peopleData.forEach((data, idx) => {
        const distribution = data.billDistributions.find(d => d.billId === bill.id);
        const amount = distribution ? distribution.amount : 0;
        const xPerson = xTot + 140 + (idx * colWidth);
        rowT(BRL.format(amount), xPerson, y);
      });

      y += Math.max(1, contaLines.length) * line;
    });

    y += line;
    ctx.strokeStyle = '#e5e7eb';
    ctx.setLineDash([5, 5]);
    ctx.beginPath();
    ctx.moveTo(pad + 10, y);
    ctx.lineTo(W - pad - 10, y);
    ctx.stroke();
    ctx.setLineDash([]);
    y += line * 2;

    section('Valores para Reserva', pad + 10, y);
    y += line;
    peopleData.forEach((d) => {
      const nameW = ctx.measureText(d.person.name + ': ').width;
      boldText(d.person.name + ': ', pad + 10, y);
      text(BRL.format(d.reserveAmount) + ` (${d.person.reservePercentage}% do salário)`, pad + 10 + nameW, y);
      y += line;
    });

    y += line * 1.2;
    section('Contas + Reserva', pad + 10, y);
    y += line;
    peopleData.forEach((d) => {
      const nameW = ctx.measureText(d.person.name + ': ').width;
      boldText(d.person.name + ': ', pad + 10, y);
      text(BRL.format(d.totalToPay), pad + 10 + nameW, y);
      y += line;
    });

    y += line * 1.2;
    section('Saldo Restante', pad + 10, y);
    y += line;
    peopleData.forEach((d) => {
      const nameW = ctx.measureText(d.person.name + ': ').width;
      boldText(d.person.name + ': ', pad + 10, y);
      const color = d.remainingSalary < 0 ? '#dc2626' : '#16a34a';
      boldText(BRL.format(d.remainingSalary), pad + 10 + nameW, y, color);
      y += line;
    });

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
        alert('Imagem copiada para a área de transferência!');
      } else {
        baixarImagem();
      }
    } catch (e) {
      baixarImagem();
    }
  };

  return (
    <div
      className="fixed inset-0 bg-black/60 backdrop-blur-sm flex items-center justify-center p-5 z-50"
      onClick={onClose}
    >
      <div
        className="bg-white dark:bg-gray-800 rounded-3xl max-w-[1000px] w-full p-5 md:p-8 shadow-2xl overflow-y-auto max-h-[95vh]"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 mb-6">
          <div>
            <h3 className="text-xl font-bold m-0 text-gray-900 dark:text-white">Relatório</h3>
            <p className="text-sm text-gray-500">Imagem para compartilhamento</p>
          </div>

          <div className="flex flex-wrap gap-2 w-full md:w-auto">
            <button
              onClick={copiarImagem}
              className="flex-1 md:flex-none px-4 py-2 bg-blue-600 text-white rounded-xl hover:bg-blue-700 transition-colors text-sm font-bold shadow-sm"
            >
              Copiar
            </button>
            <button
              className="flex-1 md:flex-none px-4 py-2 bg-gray-100 dark:bg-gray-700 text-gray-700 dark:text-gray-200 rounded-xl hover:bg-gray-200 transition-colors text-sm font-bold"
              onClick={baixarImagem}
            >
              Baixar PNG
            </button>
            <button
              className="px-4 py-2 text-gray-600 dark:text-gray-300 hover:text-gray-900 dark:hover:text-white text-sm font-bold transition-colors"
              onClick={onClose}
            >
              Fechar
            </button>
          </div>
        </div>
        <div className="flex justify-center items-start p-2 md:p-4 bg-gray-50 dark:bg-gray-900 border-2 border-dashed border-gray-200 dark:border-gray-700 rounded-2xl overflow-auto">
          {imageUrl ? (
            <img src={imageUrl} alt="Relatório" className="max-w-full h-auto shadow-lg rounded-sm" />
          ) : (
            <div className="p-20 text-gray-400">Gerando...</div>
          )}
        </div>
      </div>
    </div>
  );
};