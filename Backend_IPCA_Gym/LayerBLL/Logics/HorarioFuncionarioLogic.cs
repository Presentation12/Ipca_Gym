using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using LayerDAL;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;

namespace LayerBLL.Logics
{
    public class HorarioFuncionarioLogic
    {
        public static async Task<Response> GetHorarioFuncionarioLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<HorarioFuncionario> horarioFuncionarioList = await LayerDAL.HorarioFuncionarioService.GetHorarioFuncionariosService(sqlDataSource);
            if (horarioFuncionarioList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Sucesso na obtenção dos dados";
                response.Data = new JsonResult(horarioFuncionarioList);
            }
            return response;
        }
    }
}
