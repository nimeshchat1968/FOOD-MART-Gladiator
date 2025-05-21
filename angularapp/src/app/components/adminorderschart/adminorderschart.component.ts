import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { Chart, registerables, ChartConfiguration, ChartType } from 'chart.js';
import { OrderService } from 'src/app/services/order.service';
import { Orders } from 'src/app/models/orders.model';

@Component({
  selector: 'app-adminorderschart',
  templateUrl: './adminorderschart.component.html',
  styleUrls: ['./adminorderschart.component.css']
})
export class AdminorderschartComponent implements OnInit {
  @ViewChild('barChart', { static: true }) barChart: ElementRef<HTMLCanvasElement>;
  @ViewChild('lineChart', { static: true }) lineChart: ElementRef<HTMLCanvasElement>;
  @ViewChild('radarChart', { static: true }) radarChart: ElementRef<HTMLCanvasElement>;
  @ViewChild('doughnutChart', { static: true }) doughnutChart: ElementRef<HTMLCanvasElement>;
  @ViewChild('polarAreaChart', { static: true }) polarAreaChart: ElementRef<HTMLCanvasElement>;
  @ViewChild('pieChart', { static: true }) pieChart: ElementRef<HTMLCanvasElement>;

  isLoading: boolean = true; // Initially set loading state to true

  barChartInstance: Chart<'bar', number[], string>;
  lineChartInstance: Chart<'line', number[], string>;
  radarChartInstance: Chart<'radar', number[], string>;
  doughnutChartInstance: Chart<'doughnut', number[], string>;
  polarAreaChartInstance: Chart<'polarArea', number[], string>;
  pieChartInstance: Chart<'pie', number[], string>;

  colors: string[] = [
    'blue',
    'orange',
    'green',
    'pink',
    'cyan',
    'purple'
  ];

  polarColors: string[] = [
    'rgba(255, 99, 132, 0.2)',
    'rgba(54, 162, 235, 0.2)',
    'rgba(255, 206, 86, 0.2)',
    'rgba(75, 192, 192, 0.2)',
    'rgba(153, 102, 255, 0.2)',
    'rgba(255, 159, 64, 0.2)'
  ];

  constructor(private readonly orderService: OrderService) { }

  ngOnInit(): void {
    Chart.register(...registerables);
    this.fetchOrders();
  }

  fetchOrders(): void {
    this.isLoading = true;  // Set loading state to true when data fetching starts
    this.orderService.getAllOrders().subscribe(
      (data: Orders[]) => {
        this.processOrders(data);
        this.isLoading = false;  // Set loading state to false once data is fetched and processed
      },
      (error) => {
        console.error('Error fetching orders', error);
        this.isLoading = false;  // Set loading state to false if an error occurs
      }
    );
  }

  processOrders(orders: Orders[]): void {
    const ordersByDate = orders.reduce((acc, order) => {
      const date = new Date(order.orderDate).toLocaleDateString();
      acc[date] = (acc[date] || 0) + 1;
      return acc;
    }, {} as { [key: string]: number });

    const sortedDates = Object.keys(ordersByDate).sort((a, b) => new Date(a).getTime() - new Date(b).getTime());
    const sortedData = sortedDates.map(date => ordersByDate[date]);

    this.createChart('bar', sortedDates, sortedData, this.colors, 'Orders Per Day');
    this.createChart('line', sortedDates, sortedData, ['rgba(153, 102, 255, 0.2)'], 'Orders Per Day');
    this.createChart('radar', sortedDates, sortedData, ['rgba(255, 99, 132, 0.2)'], 'Orders Per Day');
    this.createChart('doughnut', sortedDates, sortedData, this.colors, 'Orders Per Day');
    this.createChart('polarArea', sortedDates, sortedData, this.polarColors, 'Orders Per Day');
    this.createChart('pie', sortedDates, sortedData, this.colors, 'Orders Per Day');
  }
  createChart(type: ChartType, labels: string[], data: number[], colors: string[], title: string): void {
    const scalesConfig = {};
  
    if (type === 'pie' || type === 'doughnut') {
      Object.assign(scalesConfig, {});
    } else if (type === 'radar' || type === 'polarArea') {
      Object.assign(scalesConfig, {
        r: {
          beginAtZero: true,
          ticks: {
            stepSize: 1
          }
        }
      });
    } else {
      Object.assign(scalesConfig, {
        x: {
          title: {
            display: true,
            text: 'Order Date'
          }
        },
        y: {
          beginAtZero: true,
          ticks: {
            stepSize: 1
          },
          title: {
            display: true,
            text: 'No of Orders'
          }
        }
      });
    }

    const config: ChartConfiguration<typeof type, number[], string> = {
      type: type,
      data: {
        labels: labels,
        datasets: [
          {
            data: data,
            backgroundColor: colors,
            borderColor: colors.map(color => color.replace('0.2', '1')),
            borderWidth: 1,
            fill: type === 'line' || type === 'radar'
          }
        ]
      },
      options: {
        responsive: true,
        plugins: {
          legend: {
            display: type !== 'bar' && type !== 'line' && type !== 'radar',
          },
          title: {
            display: true,
            text: title
          }
        },
        scales: scalesConfig
      }
    };
  
    switch (type) {
      case 'bar':
        this.barChartInstance = new Chart(this.barChart.nativeElement, config as ChartConfiguration<'bar', number[], string>);
        break;
      case 'line':
        this.lineChartInstance = new Chart(this.lineChart.nativeElement, config as ChartConfiguration<'line', number[], string>);
        break;
      case 'radar':
        this.radarChartInstance = new Chart(this.radarChart.nativeElement, config as ChartConfiguration<'radar', number[], string>);
        break;
      case 'doughnut':
        this.doughnutChartInstance = new Chart(this.doughnutChart.nativeElement, config as ChartConfiguration<'doughnut', number[], string>);
        break;
      case 'polarArea':
        this.polarAreaChartInstance = new Chart(this.polarAreaChart.nativeElement, config as ChartConfiguration<'polarArea', number[], string>);
        break;
      case 'pie':
        this.pieChartInstance = new Chart(this.pieChart.nativeElement, config as ChartConfiguration<'pie', number[], string>);
        break;
    }
  }
  
}
